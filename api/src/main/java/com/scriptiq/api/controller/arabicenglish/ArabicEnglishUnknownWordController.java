package com.scriptiq.api.controller.arabicenglish;

import com.scriptiq.api.dto.request.ArabicEnglishApproveWordRequest;
import com.scriptiq.api.dto.response.WordResponse;
import com.scriptiq.api.model.UnknownWord;
import com.scriptiq.api.service.shared.UnknownWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArabicEnglishUnknownWordController {

    private final UnknownWordService unknownWordService;

    @GetMapping("/v1/arabic-english-unknown-words")
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

    @GetMapping("/v1/arabic-english-unknown-words/approved")
    public List<ArabicEnglishApproveWordRequest> approvedWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .filter(UnknownWord::isApproved)
                .map(word ->
                        new ArabicEnglishApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }

    @GetMapping("/v1/arabic-english-unknown-words/review")
    public List<ArabicEnglishApproveWordRequest> review() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .map(word ->
                        new ArabicEnglishApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }
}
