package com.scriptiq.api.service.arabicenglish.feedback;

import com.scriptiq.api.model.UnknownWord;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishUnknownWordsPersistenceService;
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

        words.putAll(
                persistenceService.getWords()
        );
    }

    public void record(
            String word,
            String prediction,
            double confidence
    ) {

        words.compute(
                word,
                (key, existing) -> {

                    if (existing == null) {

                        UnknownWord unknownWord =
                                new UnknownWord();

                        unknownWord.setWord(
                                word
                        );

                        unknownWord.setCount(1);

                        unknownWord.setPrediction(
                                prediction
                        );

                        unknownWord.setConfidence(
                                confidence
                        );

                        unknownWord.setApproved(
                                confidence >= 0.95
                        );

                        return unknownWord;
                    }

                    existing.setCount(
                            existing.getCount() + 1
                    );

                    return existing;
                }
        );

        save();
    }

    public void remove(
            String word
    ) {

        words.remove(word);

        save();
    }

    private void save() {

        try {

            persistenceService.save(
                    words
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
