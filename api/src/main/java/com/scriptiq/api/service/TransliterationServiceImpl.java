package com.scriptiq.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransliterationServiceImpl implements TransliterationService {

    private final DictionaryService dictionaryService;

    @Override
    public String convert(String text) {
        String result = dictionaryService.lookup(text);

        if (result != null) {
            return result;
        }

        return text;
    }
}
