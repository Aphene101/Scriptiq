package com.scriptiq.api.service.arabicenglish.dictionary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptiq.api.service.arabicenglish.feedback.ArabicEnglishApprovedWordsService;
import com.scriptiq.api.service.frankoarabic.feedback.FrankoArabicApprovedWordsService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ArabicEnglishDictionaryServiceImpl
        implements ArabicEnglishDictionaryService {

    private final ObjectMapper objectMapper;
    private final ArabicEnglishApprovedWordsService approvedWordsService;

    private Map<String, String> dictionary =
            new HashMap<>();

    public ArabicEnglishDictionaryServiceImpl(
            ObjectMapper objectMapper,
            ArabicEnglishApprovedWordsService approvedWordsService
    ) {
        this.objectMapper = objectMapper;
        this.approvedWordsService =
                approvedWordsService;
    }

    @PostConstruct
    public void loadDictionary()
            throws Exception {

        InputStream inputStream =
                new ClassPathResource(
                        "models/arabic-english/arabic_english_dictionary.json"
                ).getInputStream();

        dictionary =
                objectMapper.readValue(
                        inputStream,
                        new TypeReference<>() {}
                );

        dictionary.putAll(
                approvedWordsService
                        .getApprovedWords()
        );

        System.out.println(
                "Loaded "
                        + approvedWordsService
                        .getApprovedWords()
                        .size()
                        + " approved words"
        );
    }

    @Override
    public String lookup(
            String text
    ) {
        return dictionary.get(
                text.toLowerCase()
        );
    }

    @Override
    public void add(
            String arabic,
            String english
    ) {

        dictionary.put(
                arabic,
                english.toLowerCase()
        );
    }
}
