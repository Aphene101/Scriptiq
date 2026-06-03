package com.scriptiq.api.service;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class OnnxTransliterationService {

    public OnnxTransliterationService(
            SourceVocabService sourceVocabService,
            TargetVocabService targetVocabService
    ) throws Exception {

        System.out.println(
                sourceVocabService
                        .getVocab()
                        .getIndex("a")
        );

        System.out.println(
                targetVocabService
                        .getVocab()
                        .getCharacter(10)
        );

        long[] encoded =
                sourceVocabService
                        .getVocab()
                        .encode("7abibi");

        for (long value : encoded) {

            System.out.print(
                    value + " "
            );
        }

        System.out.println();

        int[] tokens = {
                1,
                17,
                12,
                40,
                12,
                40,
                2
        };

        String decoded =
                targetVocabService
                        .getVocab()
                        .decode(tokens);

        System.out.println(decoded);
    }
}