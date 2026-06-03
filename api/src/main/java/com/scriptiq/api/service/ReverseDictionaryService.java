package com.scriptiq.api.service;

public interface ReverseDictionaryService {

    String lookup(String text);

    void add(
            String arabic,
            String franko
    );
}