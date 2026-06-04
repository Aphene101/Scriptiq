package com.scriptiq.api.service.frankoarabic;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class FrankoArabicSourceVocabService {

    private final Vocab vocab;

    public FrankoArabicSourceVocabService(
            ObjectMapper objectMapper
    ) throws Exception {

        vocab = new Vocab(
                "models/franko-arabic/source_vocab.json",
                objectMapper
        );
    }

}
