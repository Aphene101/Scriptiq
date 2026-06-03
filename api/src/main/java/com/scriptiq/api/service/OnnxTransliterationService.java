package com.scriptiq.api.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnnxTransliterationService {

    private final SourceVocabService sourceVocabService;
    private final TargetVocabService targetVocabService;

    private final OrtEnvironment environment;
    private final OrtSession encoderSession;
    private final OrtSession decoderSession;

    private final Map<String, String> cache =
            new ConcurrentHashMap<>();

    private final StatisticsService statisticsService;

    public OnnxTransliterationService(
            SourceVocabService sourceVocabService,
            TargetVocabService targetVocabService, StatisticsService statisticsService
    ) throws Exception {

        this.sourceVocabService = sourceVocabService;
        this.targetVocabService = targetVocabService;
        this.statisticsService = statisticsService;

        this.environment =
                OrtEnvironment.getEnvironment();

        var encoderResource =
                getClass().getResource(
                        "/models/encoder.onnx"
                );

        var decoderResource =
                getClass().getResource(
                        "/models/decoder.onnx"
                );

        assert encoderResource != null;
        this.encoderSession =
                environment.createSession(
                        Paths.get(
                                encoderResource.toURI()
                        ).toString(),
                        new OrtSession.SessionOptions()
                );

        assert decoderResource != null;
        this.decoderSession =
                environment.createSession(
                        Paths.get(
                                decoderResource.toURI()
                        ).toString(),
                        new OrtSession.SessionOptions()
                );

        System.out.println(
                "Encoder loaded"
        );

        System.out.println(
                "Decoder loaded"
        );
    }

    private int argmax(
            float[] values
    ) {

        int bestIndex = 0;

        float bestValue =
                values[0];

        for (
                int i = 1;
                i < values.length;
                i++
        ) {

            if (
                    values[i]
                            > bestValue
            ) {

                bestValue =
                        values[i];

                bestIndex = i;
            }
        }

        return bestIndex;
    }

    private String generate(
            String text
    ) throws Exception {

        long[] encoded =
                sourceVocabService
                        .getVocab()
                        .encode(text);

        try (
                OnnxTensor sourceTensor =
                        OnnxTensor.createTensor(
                                environment,
                                new long[][]{
                                        encoded
                                }
                        )
        ) {

            var encoderResult =
                    encoderSession.run(
                            Map.of(
                                    "source",
                                    sourceTensor
                            )
                    );

            float[][][] hidden =
                    (float[][][])
                            encoderResult.get(0)
                                    .getValue();

            float[][][] cell =
                    (float[][][])
                            encoderResult.get(1)
                                    .getValue();

            int sos =
                    targetVocabService
                            .getVocab()
                            .getIndex("<SOS>");

            int eos =
                    targetVocabService
                            .getVocab()
                            .getIndex("<EOS>");

            long currentToken =
                    sos;

            StringBuilder result =
                    new StringBuilder();

            for (
                    int step = 0;
                    step < 24;
                    step++
            ) {

                try (

                        OnnxTensor tokenTensor =
                                OnnxTensor.createTensor(
                                        environment,
                                        new long[]{
                                                currentToken
                                        }
                                );

                        OnnxTensor hiddenTensor =
                                OnnxTensor.createTensor(
                                        environment,
                                        hidden
                                );

                        OnnxTensor cellTensor =
                                OnnxTensor.createTensor(
                                        environment,
                                        cell
                                )

                ) {

                    var decoderResult =
                            decoderSession.run(
                                    Map.of(
                                            "token",
                                            tokenTensor,
                                            "hidden",
                                            hiddenTensor,
                                            "cell",
                                            cellTensor
                                    )
                            );

                    float[][] prediction =
                            (float[][])
                                    decoderResult.get(0)
                                            .getValue();

                    hidden =
                            (float[][][])
                                    decoderResult.get(1)
                                            .getValue();

                    cell =
                            (float[][][])
                                    decoderResult.get(2)
                                            .getValue();

                    int predictedToken =
                            argmax(
                                    prediction[0]
                            );

                    if (
                            predictedToken
                                    == eos
                    ) {
                        break;
                    }

                    String character =
                            targetVocabService
                                    .getVocab()
                                    .getCharacter(
                                            predictedToken
                                    );

                    result.append(
                            character
                    );

                    currentToken =
                            predictedToken;
                }
            }

            return result.toString();
        }
    }

    public String transliterate(
            String text
    ) throws Exception {

        String cached =
                cache.get(text);

        if (cached != null) {

            statisticsService.cacheHit();

            return cached;
        }

        String result =
                generate(text);

        cache.put(
                text,
                result
        );

        statisticsService.onnxHit();
        return result;
    }
}