package com.scriptiq.api.controller;

import com.scriptiq.api.dto.ApproveWordRequest;
import com.scriptiq.api.service.DictionaryService;
import com.scriptiq.api.service.ReverseDictionaryService;
import com.scriptiq.api.service.UnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final ReverseDictionaryService reverseDictionaryService;
    private final UnknownWordService unknownWordService;

    @PostMapping("/v1/dictionary/bulk-approve")
    public void bulkApprove(
            @RequestBody
            List<ApproveWordRequest> requests
    ) {

        requests.forEach(request -> {

            dictionaryService.add(
                    request.franko(),
                    request.arabic()
            );

            reverseDictionaryService.add(
                    request.arabic(),
                    request.franko()
            );

            unknownWordService.remove(
                    request.franko()
            );
        });
    }
}