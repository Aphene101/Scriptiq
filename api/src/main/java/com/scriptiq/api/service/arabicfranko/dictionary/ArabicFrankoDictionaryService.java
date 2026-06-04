package com.scriptiq.api.service.arabicfranko.dictionary;

public interface ArabicFrankoDictionaryService {

    String lookup(String text);

    void add(
            String arabic,
            String franko
    );
}
