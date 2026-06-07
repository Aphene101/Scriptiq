package com.scriptiq.api.controller.frankoarabic;

import com.scriptiq.api.dto.request.FrankoArabicApproveWordRequest;
import com.scriptiq.api.service.frankoarabic.dictionary.FrankoArabicDictionaryService;
import com.scriptiq.api.service.frankoarabic.feedback.FrankoArabicApprovedWordsService;
import com.scriptiq.api.service.shared.UnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FrankoArabicDictionaryController {

    private final FrankoArabicDictionaryService dictionaryService;
    private final UnknownWordService unknownWordService;
    private final FrankoArabicApprovedWordsService approvedWordsService;

    @PostMapping("/v1/franko-arabic-dictionary/bulk-approve")
    public void bulkApprove(
            @RequestBody
            List<FrankoArabicApproveWordRequest> requests
    ) throws Exception {

        for (FrankoArabicApproveWordRequest request : requests) {

            dictionaryService.add(
                    request.franko(),
                    request.arabic()
            );

            approvedWordsService.add(
                    request.franko(),
                    request.arabic()
            );

            unknownWordService.remove(
                    request.franko()
            );
        }
    }
}
