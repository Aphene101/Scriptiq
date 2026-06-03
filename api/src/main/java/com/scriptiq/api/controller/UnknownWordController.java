package com.scriptiq.api.controller;

import com.scriptiq.api.dto.UnknownWordResponse;
import com.scriptiq.api.service.UnknownWordService;
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
                .entrySet()
                .stream()
                .map(entry ->
                        new UnknownWordResponse(
                                entry.getKey(),
                                entry.getValue().get()
                        )
                )
                .sorted(
                        Comparator.comparingInt(
                                UnknownWordResponse::count
                        ).reversed()
                )
                .toList();
    }
}