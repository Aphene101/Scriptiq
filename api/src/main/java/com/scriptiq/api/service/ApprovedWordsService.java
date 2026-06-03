package com.scriptiq.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ApprovedWordsService {

    private static final Path FILE_PATH =
            Paths.get(
                    "api",
                    "data",
                    "approved_words.json"
            );

    private final ObjectMapper objectMapper;

    private Map<String, String> approvedWords =
            new HashMap<>();

    public ApprovedWordsService(
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

        approvedWords =
                objectMapper.readValue(
                        FILE_PATH.toFile(),
                        new TypeReference<>() {}
                );
    }

    public synchronized void add(
            String franko,
            String arabic
    ) throws Exception {

        approvedWords.put(
                franko.toLowerCase(),
                arabic
        );

        save();
    }

    private void save() throws Exception {

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(
                        FILE_PATH.toFile(),
                        approvedWords
                );
    }
}