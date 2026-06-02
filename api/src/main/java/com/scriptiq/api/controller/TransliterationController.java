package com.scriptiq.api.controller;

import com.scriptiq.api.dto.ConvertRequest;
import com.scriptiq.api.dto.ConvertResponse;
import com.scriptiq.api.service.TransliterationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TransliterationController {

    private final TransliterationService transliterationService;

    @PostMapping("/convert")
    public ConvertResponse convert(
            @Valid @RequestBody ConvertRequest request
    ) {
        return new ConvertResponse(
                transliterationService.convert(request.text())
        );
    }
}
