package com.scriptiq.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class TargetVocabService {

    private final Vocab vocab;

    public TargetVocabService(
            ObjectMapper objectMapper
    ) throws Exception {

        vocab = new Vocab(
                "vocab/target_vocab.json",
                objectMapper
        );
    }

}