package com.scriptiq.api.dto;

public record UnknownWordResponse(
        String word,
        String prediction,
        double confidence,
        int count,
        boolean approved
) {
}