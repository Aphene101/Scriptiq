package com.scriptiq.api.service.arabicenglish.dictionary;

public interface ArabicEnglishDictionaryService {
    String lookup(String text);

    void add(
            String arabic,
            String english
    );
}
