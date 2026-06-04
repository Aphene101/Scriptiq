package com.scriptiq.api.service.arabicenglish.feedback;

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
public class ArabicEnglishUnknownWordService {

    private final ArabicEnglishUnknownWordsPersistenceService
            persistenceService;

    private final Map<String, UnknownWord> words =
            new ConcurrentHashMap<>();

    @PostConstruct
    public void load() {
        words.putAll(persistenceService.getWords());
    }
}
