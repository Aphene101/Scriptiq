package com.scriptiq.api.service;

public interface DictionaryService {

    String lookup(String text);

    void add(
            String franko,
            String arabic
    );
}