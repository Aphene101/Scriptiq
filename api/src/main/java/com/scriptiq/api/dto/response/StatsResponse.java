package com.scriptiq.api.dto.response;

public record StatsResponse(
        long dictionaryHits,
        long cacheHits,
        long onnxHits
) {
}
