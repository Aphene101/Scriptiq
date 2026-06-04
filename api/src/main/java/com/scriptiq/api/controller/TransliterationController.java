package com.scriptiq.api.controller;

import com.scriptiq.api.dto.request.TransliterationRequest;
import com.scriptiq.api.dto.response.TransliterationResponse;
import com.scriptiq.api.service.arabicfranko.ArabicFrankoService;
import com.scriptiq.api.service.frankoarabic.FrankoArabicService;
import com.scriptiq.api.service.shared.InputTypeDetector;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TransliterationController {

    private final FrankoArabicService frankoArabicService;
    private final ArabicFrankoService arabicFrankoService;
    private final InputTypeDetector inputTypeDetector;

    @PostMapping("/transliterate")
    public TransliterationResponse convert(
            @Valid @RequestBody TransliterationRequest request
    ) {
        String result = inputTypeDetector.isArabic(request.text())
                ? arabicFrankoService.convert(request.text())
                : frankoArabicService.convert(request.text());

        return new TransliterationResponse(
                result
        );
    }
}
