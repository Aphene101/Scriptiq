package com.scriptiq.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptiq.api.model.UnknownWord;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Getter
@Service
public class UnknownWordsPersistenceService {

    private static final Path FILE_PATH =
            Paths.get(
                    "api",
                    "data",
                    "unknown_words.json"
            );

    private final ObjectMapper objectMapper;

    private Map<String, UnknownWord> words =
            new HashMap<>();

    public UnknownWordsPersistenceService(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void load() throws Exception {

        if (!Files.exists(FILE_PATH)) {

            Files.createDirectories(
                    FILE_PATH.getParent()
            );

            Files.writeString(
                    FILE_PATH,
                    "{}"
            );
        }

        words =
                objectMapper.readValue(
                        FILE_PATH.toFile(),
                        new TypeReference<
                                Map<String, UnknownWord>
                                >() {}
                );
    }

    public synchronized void save(
            Map<String, UnknownWord> words
    ) throws Exception {

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(
                        FILE_PATH.toFile(),
                        words
                );

        this.words =
                new HashMap<>(words);
    }
}