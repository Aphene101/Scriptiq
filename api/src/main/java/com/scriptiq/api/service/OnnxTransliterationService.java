package com.scriptiq.api.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Map;

@Service
public class OnnxTransliterationService {

    private final SourceVocabService sourceVocabService;
    private final TargetVocabService targetVocabService;

    private final OrtEnvironment environment;
    private final OrtSession encoderSession;
    private final OrtSession decoderSession;

    public OnnxTransliterationService(
            SourceVocabService sourceVocabService,
            TargetVocabService targetVocabService
    ) throws Exception {

        this.sourceVocabService = sourceVocabService;
        this.targetVocabService = targetVocabService;

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

        testEncoder();
        testDecoder();
    }

    private void testEncoder()
            throws Exception {

        long[] encoded =
                sourceVocabService
                        .getVocab()
                        .encode("7abibi");

        try (
                OnnxTensor sourceTensor =
                        OnnxTensor.createTensor(
                                environment,
                                new long[][]{
                                        encoded
                                }
                        )
        ) {

            var result =
                    encoderSession.run(
                            Map.of(
                                    "source",
                                    sourceTensor
                            )
                    );

            System.out.println();
            System.out.println(
                    "Encoder executed"
            );

            float[][][] hidden =
                    (float[][][])
                            result.get(0)
                                    .getValue();

            float[][][] cell =
                    (float[][][])
                            result.get(1)
                                    .getValue();

            System.out.println(
                    "Hidden shape: "
                            + hidden.length
                            + ", "
                            + hidden[0].length
                            + ", "
                            + hidden[0][0].length
            );

            System.out.println(
                    "Cell shape: "
                            + cell.length
                            + ", "
                            + cell[0].length
                            + ", "
                            + cell[0][0].length
            );
        }
    }

    private void testDecoder()
            throws Exception {

        long[] encoded =
                sourceVocabService
                        .getVocab()
                        .encode("7abibi");

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

            long sosToken =
                    targetVocabService
                            .getVocab()
                            .getIndex("<SOS>");

            try (

                    OnnxTensor tokenTensor =
                            OnnxTensor.createTensor(
                                    environment,
                                    new long[]{
                                            sosToken
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

                System.out.println();
                System.out.println(
                        "Decoder executed"
                );

                System.out.println(
                        "Prediction size: "
                                + prediction[0].length
                );
            }
        }
    }
}