package com.scriptiq.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReverseDictionaryServiceImpl
        implements ReverseDictionaryService {

    private final ObjectMapper objectMapper;
    private final ApprovedWordsService approvedWordsService;

    private Map<String, String> dictionary =
            new HashMap<>();

    public ReverseDictionaryServiceImpl(
            ObjectMapper objectMapper,
            ApprovedWordsService approvedWordsService
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
                        "reverse_dictionary.json"
                ).getInputStream();

        dictionary =
                objectMapper.readValue(
                        inputStream,
                        new TypeReference<>() {}
                );

        approvedWordsService
                .getApprovedWords()
                .forEach((franko, arabic) ->

                        dictionary.putIfAbsent(
                                arabic,
                                franko
                        )
                );

        System.out.println(
                "Loaded "
                        + approvedWordsService
                        .getApprovedWords()
                        .size()
                        + " approved reverse words"
        );
    }

    @Override
    public String lookup(
            String text
    ) {
        return dictionary.get(text);
    }

    @Override
    public void add(
            String arabic,
            String franko
    ) {

        dictionary.putIfAbsent(
                arabic,
                franko
        );
    }
}