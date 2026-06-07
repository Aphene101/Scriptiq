package com.scriptiq.api.controller.frankoarabic;

import com.scriptiq.api.dto.request.FrankoArabicApproveWordRequest;
import com.scriptiq.api.service.arabicfranko.dictionary.ArabicFrankoDictionaryService;
import com.scriptiq.api.service.arabicfranko.feedback.ArabicFrankoApprovedWordsService;
import com.scriptiq.api.service.frankoarabic.dictionary.FrankoArabicDictionaryService;
import com.scriptiq.api.service.frankoarabic.feedback.FrankoArabicApprovedWordsService;
import com.scriptiq.api.service.frankoarabic.feedback.FrankoArabicUnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FrankoArabicDictionaryController {

    private final FrankoArabicDictionaryService dictionaryService;
    private final ArabicFrankoDictionaryService arabicFrankoDictionaryService;
    private final FrankoArabicUnknownWordService unknownWordService;
    private final FrankoArabicApprovedWordsService approvedWordsService;
    private final ArabicFrankoApprovedWordsService arabicFrankoApprovedWordsService;

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

            arabicFrankoDictionaryService.add(
                    request.arabic(),
                    request.franko()
            );

            approvedWordsService.add(
                    request.franko(),
                    request.arabic()
            );

            arabicFrankoApprovedWordsService.add(
                    request.arabic(),
                    request.franko()
            );

            unknownWordService.remove(
                    request.franko()
            );
        }
    }
}
