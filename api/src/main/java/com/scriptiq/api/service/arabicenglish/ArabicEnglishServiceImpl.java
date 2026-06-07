package com.scriptiq.api.service.arabicenglish;

import com.scriptiq.api.service.arabicenglish.dictionary.ArabicEnglishDictionaryService;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishUnknownWordService;
import com.scriptiq.api.service.shared.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArabicEnglishServiceImpl
        implements ArabicEnglishService {

    private final ArabicEnglishDictionaryService dictionaryService;
    private final ArabicEnglishOnnxService onnxService;
    private final StatisticsService statisticsService;
    private final ArabicEnglishUnknownWordService unknownWordService;

    @Override
    public String convert(String text) {

        if (text == null || text.isBlank()) {
            return text;
        }

        StringBuilder result = new StringBuilder();

        String[] words = text.split("\\s+");

        for (int i = 0; i < words.length; i++) {

            String word = words[i];

            String converted =
                    dictionaryService.lookup(
                            word.toLowerCase()
                    );

            if (converted != null) {
                statisticsService.dictionaryHit();
            }

            if (converted == null) {
                try {
                    long start = System.nanoTime();

                    var prediction =
                            onnxService.transliterate(
                                    word.toLowerCase()
                            );

                    converted = prediction.text();

                    unknownWordService.record(
                            word.toLowerCase(),
                            prediction.text(),
                            prediction.confidence()
                    );

                    long end = System.nanoTime();

                    System.out.println(
                            "ONNX took "
                                    + ((end - start) / 1_000_000)
                                    + " ms"
                    );

                } catch (Exception e) {

                    converted = word;
                }
            }

            result.append(converted);

            if (i < words.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}
