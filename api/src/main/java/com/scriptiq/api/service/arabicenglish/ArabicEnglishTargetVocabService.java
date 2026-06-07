package com.scriptiq.api.service.arabicenglish;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptiq.api.service.shared.Vocab;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ArabicEnglishTargetVocabService {

    private final Vocab vocab;

    public ArabicEnglishTargetVocabService(
            ObjectMapper objectMapper
    ) throws Exception {

        vocab = new Vocab(
                "models/arabic-english/arabic_english_target_vocab.json",
                objectMapper
        );
    }

}
