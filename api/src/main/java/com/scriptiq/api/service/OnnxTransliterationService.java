package com.scriptiq.api.service;

import ai.onnxruntime.*;

import org.springframework.stereotype.Service;

@Service
public class OnnxTransliterationService {

    public OnnxTransliterationService() throws Exception {

        OrtEnvironment env =
                OrtEnvironment.getEnvironment();

        OrtSession session =
                env.createSession(
                        getClass()
                                .getResource("/models/model.onnx")
                                .getPath(),
                        new OrtSession.SessionOptions()
                );

        System.out.println(
                "ONNX model loaded successfully"
        );
    }
}