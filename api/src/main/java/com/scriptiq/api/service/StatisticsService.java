package com.scriptiq.api.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatisticsService {

    private final AtomicLong dictionaryHits =
            new AtomicLong();

    private final AtomicLong cacheHits =
            new AtomicLong();

    private final AtomicLong onnxHits =
            new AtomicLong();

    public void dictionaryHit() {
        dictionaryHits.incrementAndGet();
    }

    public void cacheHit() {
        cacheHits.incrementAndGet();
    }

    public void onnxHit() {
        onnxHits.incrementAndGet();
    }

    public long getDictionaryHits() {
        return dictionaryHits.get();
    }

    public long getCacheHits() {
        return cacheHits.get();
    }

    public long getOnnxHits() {
        return onnxHits.get();
    }
}