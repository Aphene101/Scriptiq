package com.scriptiq.api.controller.arabicenglish;

import com.scriptiq.api.dto.request.FrankoArabicApproveWordRequest;
import com.scriptiq.api.dto.response.FrankoArabicUnknownWordResponse;
import com.scriptiq.api.model.UnknownWord;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishUnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArabicEnglishUnknownWordController {

    private final ArabicEnglishUnknownWordService unknownWordService;

    @GetMapping("/v1/arabic-english-unknown-words")
    public List<FrankoArabicUnknownWordResponse> unknownWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .map(word ->
                        new FrankoArabicUnknownWordResponse(
                                word.getWord(),
                                word.getPrediction(),
                                word.getConfidence(),
                                word.getCount(),
                                word.isApproved()
                        )
                )
                .sorted(
                        Comparator.comparingInt(
                                FrankoArabicUnknownWordResponse::count
                        ).reversed()
                )
                .toList();
    }

    @GetMapping("/v1/arabic-english-unknown-words/approved")
    public List<FrankoArabicApproveWordRequest> approvedWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .filter(UnknownWord::isApproved)
                .map(word ->
                        new FrankoArabicApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }

    @GetMapping("/v1/arabic-english-unknown-words/review")
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
