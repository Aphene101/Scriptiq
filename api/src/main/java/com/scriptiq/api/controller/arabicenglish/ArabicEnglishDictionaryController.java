package com.scriptiq.api.controller.arabicenglish;

import com.scriptiq.api.dto.request.ArabicEnglishApproveWordRequest;
import com.scriptiq.api.service.arabicenglish.dictionary.ArabicEnglishDictionaryService;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishApprovedWordsService;
import com.scriptiq.api.service.shared.UnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArabicEnglishDictionaryController {

    private final ArabicEnglishDictionaryService dictionaryService;
    private final UnknownWordService unknownWordService;
    private final ArabicEnglishApprovedWordsService approvedWordsService;

    @PostMapping("/v1/arabic-english-dictionary/bulk-approve")
    public void bulkApprove(
            @RequestBody
            List<ArabicEnglishApproveWordRequest> requests
    ) throws Exception {

        for (ArabicEnglishApproveWordRequest request : requests) {

            dictionaryService.add(
                    request.arabic(),
                    request.english()
            );

            approvedWordsService.add(
                    request.arabic(),
                    request.english()
            );

            unknownWordService.remove(
                    request.arabic()
            );
        }
    }
}
