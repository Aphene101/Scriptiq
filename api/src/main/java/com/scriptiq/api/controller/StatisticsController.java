package com.scriptiq.api.controller;

import com.scriptiq.api.dto.StatsResponse;
import com.scriptiq.api.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/v1/stats")
    public StatsResponse stats() {

        return new StatsResponse(
                statisticsService.getDictionaryHits(),
                statisticsService.getCacheHits(),
                statisticsService.getOnnxHits()
        );
    }
}