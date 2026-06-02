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
public class DictionaryServiceImpl implements DictionaryService {

    private final ObjectMapper objectMapper;

    private Map<String, String> dictionary = new HashMap<>();

    public DictionaryServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        System.out.println("DICTIONARY SERVICE CREATED");
    }

    @PostConstruct
    public void loadDictionary() throws Exception {
        InputStream inputStream =
                new ClassPathResource("dictionary.json").getInputStream();

        dictionary = objectMapper.readValue(
                inputStream,
                new TypeReference<Map<String, String>>() {}
        );

        System.out.println(
                "Loaded dictionary entries: " + dictionary.size()
        );
    }

    @Override
    public String lookup(String text) {
        return dictionary.get(text.toLowerCase());
    }
}