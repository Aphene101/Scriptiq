package com.scriptiq.api.dto;

public record StatsResponse(
        long dictionaryHits,
        long cacheHits,
        long onnxHits
) {
}