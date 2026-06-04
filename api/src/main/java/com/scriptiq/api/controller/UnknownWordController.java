package com.scriptiq.api.controller;

import com.scriptiq.api.dto.request.ApproveWordRequest;
import com.scriptiq.api.dto.response.UnknownWordResponse;
import com.scriptiq.api.model.UnknownWord;
import com.scriptiq.api.service.frankoarabic.feedback.UnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UnknownWordController {

    private final UnknownWordService unknownWordService;

    @GetMapping("/v1/unknown-words")
    public List<UnknownWordResponse> unknownWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .map(word ->
                        new UnknownWordResponse(
                                word.getWord(),
                                word.getPrediction(),
                                word.getConfidence(),
                                word.getCount(),
                                word.isApproved()
                        )
                )
                .sorted(
                        Comparator.comparingInt(
                                UnknownWordResponse::count
                        ).reversed()
                )
                .toList();
    }

    @GetMapping("/v1/unknown-words/approved")
    public List<ApproveWordRequest> approvedWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .filter(
                        UnknownWord::isApproved
                )
                .map(word ->
                        new ApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }

    @GetMapping("/v1/unknown-words/review")
    public List<ApproveWordRequest> review() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .map(word ->
                        new ApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }
}
