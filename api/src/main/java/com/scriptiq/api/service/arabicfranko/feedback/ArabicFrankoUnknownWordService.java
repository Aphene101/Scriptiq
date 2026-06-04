package com.scriptiq.api.service.arabicfranko.feedback;

import com.scriptiq.api.model.UnknownWord;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
@RequiredArgsConstructor
public class ArabicFrankoUnknownWordService {

    private final ArabicFrankoUnknownWordsPersistenceService
            persistenceService;

    private final Map<String, UnknownWord> words =
            new ConcurrentHashMap<>();

    @PostConstruct
    public void load() {
        words.putAll(persistenceService.getWords());
    }
}
