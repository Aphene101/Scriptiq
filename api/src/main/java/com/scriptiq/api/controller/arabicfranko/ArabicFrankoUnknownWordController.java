package com.scriptiq.api.controller.arabicfranko;

import com.scriptiq.api.dto.request.ArabicEnglishApproveWordRequest;
import com.scriptiq.api.dto.request.ArabicFrankoApproveWordRequest;
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
public class ArabicFrankoUnknownWordController {

    private final UnknownWordService unknownWordService;

    @GetMapping("/v1/arabic-franko-unknown-words")
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

    @GetMapping("/v1/arabic-franko-unknown-words/approved")
    public List<ArabicFrankoApproveWordRequest> approvedWords() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .filter(UnknownWord::isApproved)
                .map(word ->
                        new ArabicFrankoApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }

    @GetMapping("/v1/arabic-franko-unknown-words/review")
    public List<ArabicFrankoApproveWordRequest> review() {

        return unknownWordService
                .getWords()
                .values()
                .stream()
                .map(word ->
                        new ArabicFrankoApproveWordRequest(
                                word.getWord(),
                                word.getPrediction()
                        )
                )
                .toList();
    }
}
