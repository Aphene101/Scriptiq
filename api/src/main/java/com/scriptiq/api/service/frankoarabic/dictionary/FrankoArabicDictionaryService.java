package com.scriptiq.api.service.frankoarabic.dictionary;

public interface FrankoArabicDictionaryService {

    String lookup(String text);

    void add(
            String franko,
            String arabic
    );
}
