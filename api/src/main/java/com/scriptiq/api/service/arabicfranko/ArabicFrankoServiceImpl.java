package com.scriptiq.api.service.arabicfranko;

import com.scriptiq.api.service.frankoarabic.dictionary.FrankoArabicReverseDictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArabicFrankoServiceImpl implements ArabicFrankoService {

    private final FrankoArabicReverseDictionaryService reverseDictionaryService;

    @Override
    public String convert(
            String text
    ) {

        if (text == null || text.isBlank()) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String converted = reverseDictionaryService.lookup(word);

            result.append(converted == null ? word : converted);

            if (i < words.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}
