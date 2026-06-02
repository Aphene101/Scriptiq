package com.scriptiq.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ConvertRequest(
        @NotBlank
        String text
) {}
