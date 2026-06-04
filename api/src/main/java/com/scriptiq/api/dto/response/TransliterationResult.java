package com.scriptiq.api.dto.response;

public record TransliterationResult(
        String text,
        double confidence
) {
}
