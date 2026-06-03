package com.scriptiq.api.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Service
public class UnknownWordService {

    private final Map<String, AtomicInteger> words =
            new ConcurrentHashMap<>();

    public void record(
            String word
    ) {

        words.computeIfAbsent(
                word,
                w -> new AtomicInteger()
        ).incrementAndGet();
    }

}