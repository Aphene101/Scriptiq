package com.scriptiq.api.controller.arabicfranko;

import com.scriptiq.api.dto.request.ArabicEnglishApproveWordRequest;
import com.scriptiq.api.dto.request.ArabicFrankoApproveWordRequest;
import com.scriptiq.api.service.arabicenglish.dictionary.ArabicEnglishDictionaryService;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishApprovedWordsService;
import com.scriptiq.api.service.arabicfranko.dictionary.ArabicFrankoDictionaryService;
import com.scriptiq.api.service.arabicfranko.feedback.ArabicFrankoApprovedWordsService;
import com.scriptiq.api.service.shared.UnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArabicFrankoDictionaryController {

    private final ArabicFrankoDictionaryService dictionaryService;
    private final UnknownWordService unknownWordService;
    private final ArabicFrankoApprovedWordsService approvedWordsService;

    @PostMapping("/v1/arabic-franko-dictionary/bulk-approve")
    public void bulkApprove(
            @RequestBody
            List<ArabicFrankoApproveWordRequest> requests
    ) throws Exception {

        for (ArabicFrankoApproveWordRequest request : requests) {

            dictionaryService.add(
                    request.arabic(),
                    request.franko()
            );

            approvedWordsService.add(
                    request.arabic(),
                    request.franko()
            );

            unknownWordService.remove(
                    request.arabic()
            );
        }
    }
}
