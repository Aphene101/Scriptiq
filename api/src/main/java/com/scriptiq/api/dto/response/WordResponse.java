package com.scriptiq.api.dto.response;

public record WordResponse(
        String word,
        String prediction,
        double confidence,
        int count,
        boolean approved
) {
}
