package com.scriptiq.api.controller;

import com.scriptiq.api.dto.request.ApproveWordRequest;
import com.scriptiq.api.service.frankoarabic.dictionary.FrankoArabicDictionaryService;
import com.scriptiq.api.service.frankoarabic.dictionary.FrankoArabicReverseDictionaryService;
import com.scriptiq.api.service.frankoarabic.feedback.ApprovedWordsService;
import com.scriptiq.api.service.frankoarabic.feedback.UnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final FrankoArabicDictionaryService dictionaryService;
    private final FrankoArabicReverseDictionaryService reverseDictionaryService;
    private final UnknownWordService unknownWordService;
    private final ApprovedWordsService approvedWordsService;

    @PostMapping("/v1/dictionary/bulk-approve")
    public void bulkApprove(
            @RequestBody
            List<ApproveWordRequest> requests
    ) throws Exception {

        for (ApproveWordRequest request : requests) {

            dictionaryService.add(
                    request.franko(),
                    request.arabic()
            );

            reverseDictionaryService.add(
                    request.arabic(),
                    request.franko()
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
