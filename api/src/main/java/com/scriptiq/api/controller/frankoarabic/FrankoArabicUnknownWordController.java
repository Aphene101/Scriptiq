package com.scriptiq.api.controller.frankoarabic;

import com.scriptiq.api.dto.request.FrankoArabicApproveWordRequest;
import com.scriptiq.api.dto.response.WordResponse;
import com.scriptiq.api.model.UnknownWord;
import com.scriptiq.api.service.frankoarabic.feedback.FrankoArabicUnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FrankoArabicUnknownWordController {

    private final FrankoArabicUnknownWordService unknownWordService;

    @GetMapping("/v1/franko-arabic-unknown-words")
    public List<WordResponse> unknownWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .map(word ->
                        new WordResponse(
                                word.getWord(),
                                word.getPrediction(),
                                word.getConfidence(),
                                word.getCount(),
                                word.isApproved()
                        )
                )
                .sorted(
                        Comparator.comparingInt(
                                WordResponse::count
                        ).reversed()
                )
                .toList();
    }

    @GetMapping("/v1/franko-arabic-unknown-words/approved")
    public List<FrankoArabicApproveWordRequest> approvedWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .filter(
                        UnknownWord::isApproved
                )
                .map(word ->
                        new FrankoArabicApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }

    @GetMapping("/v1/franko-arabic-unknown-words/review")
    public List<FrankoArabicApproveWordRequest> review() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .map(word ->
                        new FrankoArabicApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }
}
