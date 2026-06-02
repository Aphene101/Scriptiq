package com.scriptiq.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransliterationServiceImpl
        implements TransliterationService {

    private final DictionaryService dictionaryService;
    private final ReverseDictionaryService reverseDictionaryService;
    private final InputTypeDetector inputTypeDetector;

    @Override
    public String convert(String text) {

        if (text == null || text.isBlank()) {
            return text;
        }

        StringBuilder result = new StringBuilder();

        String[] words = text.split("\\s+");

        boolean arabicInput =
                inputTypeDetector.isArabic(text);

        for (int i = 0; i < words.length; i++) {

            String word = words[i];

            String converted;

            if (arabicInput) {

                converted =
                        reverseDictionaryService.lookup(word);

            } else {

                converted =
                        dictionaryService.lookup(
                                word.toLowerCase()
                        );
            }

            if (converted == null) {
                converted = word;
            }

            result.append(converted);

            if (i < words.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}