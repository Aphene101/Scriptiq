package com.scriptiq.api.service.shared;

import com.scriptiq.api.model.UnknownWord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
@RequiredArgsConstructor
public class UnknownWordService {
    private final Map<String, UnknownWord>
            words = new ConcurrentHashMap<>();

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
    }

    public void remove(
            String word
    ) {
        words.remove(word);
    }
}
