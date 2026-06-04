package com.scriptiq.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TransliterationRequest(
        @NotBlank
        String text
) {}
