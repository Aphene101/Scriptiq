package com.scriptiq.api.service.frankoarabic.dictionary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptiq.api.service.frankoarabic.feedback.FrankoArabicApprovedWordsService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class FrankoArabicDictionaryServiceImpl
        implements FrankoArabicDictionaryService {

    private final ObjectMapper objectMapper;
    private final FrankoArabicApprovedWordsService approvedWordsService;

    private Map<String, String> dictionary =
            new HashMap<>();

    public FrankoArabicDictionaryServiceImpl(
            ObjectMapper objectMapper,
            FrankoArabicApprovedWordsService approvedWordsService
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
                        "models/franko-arabic/franko_arabic_dictionary.json"
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
            String franko,
            String arabic
    ) {

        dictionary.put(
                franko.toLowerCase(),
                arabic
        );
    }
}
