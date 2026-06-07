package com.scriptiq.api.controller.arabicenglish;

import com.scriptiq.api.dto.request.FrankoArabicApproveWordRequest;
import com.scriptiq.api.service.arabicenglish.dictionary.ArabicEnglishDictionaryService;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishApprovedWordsService;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishUnknownWordService;
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
public class ArabicEnglishDictionaryController {

    private final ArabicEnglishDictionaryService dictionaryService;
    private final ArabicEnglishDictionaryService arabicEnglishDictionaryService;
    private final ArabicEnglishUnknownWordService unknownWordService;
    private final ArabicEnglishApprovedWordsService approvedWordsService;
    private final ArabicEnglishApprovedWordsService arabicEnglishApprovedWordsService;

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

            arabicEnglishDictionaryService.add(
                    request.arabic(),
                    request.franko()
            );

            approvedWordsService.add(
                    request.franko(),
                    request.arabic()
            );

            arabicEnglishApprovedWordsService.add(
                    request.arabic(),
                    request.franko()
            );

            unknownWordService.remove(
                    request.franko()
            );
        }
    }
}
