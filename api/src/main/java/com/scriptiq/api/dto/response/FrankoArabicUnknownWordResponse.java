package com.scriptiq.api.dto.response;

public record FrankoArabicUnknownWordResponse(
        String word,
        String prediction,
        double confidence,
        int count,
        boolean approved
) {
}
