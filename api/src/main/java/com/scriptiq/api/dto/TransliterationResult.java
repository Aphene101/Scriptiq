package com.scriptiq.api.dto;

public record TransliterationResult(
        String text,
        double confidence
) {
}