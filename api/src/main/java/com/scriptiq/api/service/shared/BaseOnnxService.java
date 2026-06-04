package com.scriptiq.api.service.shared;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import com.scriptiq.api.model.TransliterationResult;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseOnnxService {

    protected final OrtEnvironment environment;
    protected final OrtSession encoderSession;
    protected final OrtSession decoderSession;

    private final Map<String, TransliterationResult> cache =
            new ConcurrentHashMap<>();

    private final StatisticsService statisticsService;

    protected BaseOnnxService(
            String encoderPath,
            String decoderPath,
            StatisticsService statisticsService
    ) throws Exception {

        this.statisticsService = statisticsService;
        this.environment = OrtEnvironment.getEnvironment();
        this.encoderSession = createSession(encoderPath);
        this.decoderSession = createSession(decoderPath);
    }

    protected abstract TransliterationResult generate(
            String text
    ) throws Exception;

    public TransliterationResult transliterate(
            String text
    ) throws Exception {

        TransliterationResult cached = cache.get(text);

        if (cached != null) {
            statisticsService.cacheHit();
            return cached;
        }

        TransliterationResult result = generate(text);

        cache.put(text, result);
        statisticsService.onnxHit();

        return result;
    }

    protected int argmax(
            float[] values
    ) {

        int bestIndex = 0;
        float bestValue = values[0];

        for (int i = 1; i < values.length; i++) {
            if (values[i] > bestValue) {
                bestValue = values[i];
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    protected double confidence(
            float[] values,
            int selectedIndex
    ) {

        double sum = 0;

        for (float value : values) {
            sum += Math.exp(value);
        }

        return Math.exp(values[selectedIndex]) / sum;
    }

    private OrtSession createSession(
            String resourcePath
    ) throws Exception {

        var resource = getClass().getResource(resourcePath);

        if (resource == null) {
            throw new IllegalStateException(
                    "ONNX resource not found: " + resourcePath
            );
        }

        return environment.createSession(
                Paths.get(resource.toURI()).toString(),
                new OrtSession.SessionOptions()
        );
    }
}
