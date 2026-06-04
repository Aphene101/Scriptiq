package com.scriptiq.api.dto.response;

public record UnknownWordResponse(
        String word,
        String prediction,
        double confidence,
        int count,
        boolean approved
) {
}
