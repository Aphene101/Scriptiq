package com.scriptiq.api.service.frankoarabic;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class FrankoArabicTargetVocabService {

    private final Vocab vocab;

    public FrankoArabicTargetVocabService(
            ObjectMapper objectMapper
    ) throws Exception {

        vocab = new Vocab(
                "models/franko-arabic/franko_arabic_target_vocab.json",
                objectMapper
        );
    }

}
