package com.scriptiq.api.model;

public record TransliterationResult(
        String text,
        double confidence
) {
}