package com.scriptiq.api.controller;

import com.scriptiq.api.dto.request.TransliterationRequest;
import com.scriptiq.api.dto.response.TransliterationResponse;
import com.scriptiq.api.service.frankoarabic.FrankoArabicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/franko-arabic")
@RequiredArgsConstructor
public class FrankoArabicController {

    private final FrankoArabicService frankoArabicService;

    @PostMapping
    public TransliterationResponse convert(
            @Valid @RequestBody TransliterationRequest request
    ) {
        return new TransliterationResponse(
                frankoArabicService.convert(request.text())
        );
    }
}
