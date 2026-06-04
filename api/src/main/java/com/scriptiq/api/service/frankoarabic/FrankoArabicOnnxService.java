package com.scriptiq.api.service.frankoarabic;

import ai.onnxruntime.OnnxTensor;
import com.scriptiq.api.model.TransliterationResult;
import com.scriptiq.api.service.shared.BaseOnnxService;
import com.scriptiq.api.service.shared.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FrankoArabicOnnxService extends BaseOnnxService {

    private final FrankoArabicSourceVocabService sourceVocabService;
    private final FrankoArabicTargetVocabService targetVocabService;

    public FrankoArabicOnnxService(
            FrankoArabicSourceVocabService sourceVocabService,
            FrankoArabicTargetVocabService targetVocabService,
            StatisticsService statisticsService
    ) throws Exception {

        super(
                "/models/franko-arabic/encoder.onnx",
                "/models/franko-arabic/decoder.onnx",
                statisticsService
        );

        this.sourceVocabService = sourceVocabService;
        this.targetVocabService = targetVocabService;
    }

    @Override
    protected TransliterationResult generate(
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

            double confidenceSum = 0;
            int confidenceCount = 0;

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

                    double tokenConfidence =
                            confidence(
                                    prediction[0],
                                    predictedToken
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

                    confidenceSum +=
                            tokenConfidence;

                    confidenceCount++;

                    currentToken =
                            predictedToken;
                }
            }

            double averageConfidence =
                    confidenceCount == 0
                            ? 0
                            : confidenceSum
                              / confidenceCount;

            return new TransliterationResult(
                    result.toString(),
                    averageConfidence
            );
        }
    }

}
