package com.scriptiq.api.dto;

public record UnknownWordResponse(
        String word,
        String prediction,
        int count,
        boolean approved
) {
}