package com.scriptiq.api.service.frankoarabic.dictionary;

public interface FrankoArabicReverseDictionaryService {

    String lookup(String text);

    void add(
            String arabic,
            String franko
    );
}
