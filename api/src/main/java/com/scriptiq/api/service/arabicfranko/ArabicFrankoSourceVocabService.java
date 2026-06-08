package com.scriptiq.api.service.arabicfranko;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptiq.api.service.shared.Vocab;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ArabicFrankoSourceVocabService {

    private final Vocab vocab;

    public ArabicFrankoSourceVocabService(
            ObjectMapper objectMapper
    ) throws Exception {

        vocab = new Vocab(
                "models/arabic-franko/arabic_franko_source_vocab.json",
                objectMapper
        );
    }

}
