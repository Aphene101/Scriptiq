package com.scriptiq.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class SourceVocabService {

    private final Vocab vocab;

    public SourceVocabService(
            ObjectMapper objectMapper
    ) throws Exception {

        vocab = new Vocab(
                "vocab/source_vocab.json",
                objectMapper
        );
    }

}