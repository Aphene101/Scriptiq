package com.scriptiq.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Vocab {

    @Getter
    private final Map<String, Integer> charToIdx;
    private final Map<Integer, String> idxToChar;

    public Vocab(
            String resourcePath,
            ObjectMapper objectMapper
    ) throws Exception {

        InputStream inputStream =
                new ClassPathResource(
                        resourcePath
                ).getInputStream();

        charToIdx = objectMapper.readValue(
                inputStream,
                new TypeReference<>() {}
        );

        idxToChar = new HashMap<>();

        charToIdx.forEach(
                (key, value) ->
                        idxToChar.put(
                                value,
                                key
                        )
        );
    }

    public Integer getIndex(
            String character
    ) {
        return charToIdx.get(character);
    }

    public String getCharacter(
            Integer index
    ) {
        return idxToChar.get(index);
    }

    public long[] encode(
            String text
    ) {

        long[] result = new long[24];

        result[0] =
                charToIdx.get("<SOS>");

        int position = 1;

        for (char c : text.toCharArray()) {

            if (position >= 23) {
                break;
            }

            String character =
                    String.valueOf(c);

            result[position++] =
                    charToIdx.getOrDefault(
                            character,
                            charToIdx.get("<UNK>")
                    );
        }

        result[position] =
                charToIdx.get("<EOS>");

        return result;
    }

    public String decode(
            int[] tokens
    ) {

        StringBuilder result =
                new StringBuilder();

        for (int token : tokens) {

            String character =
                    idxToChar.get(token);

            if (character == null) {
                continue;
            }

            if (character.equals("<PAD>")) {
                continue;
            }

            if (character.equals("<SOS>")) {
                continue;
            }

            if (character.equals("<EOS>")) {
                continue;
            }

            result.append(character);
        }

        return result.toString();
    }
}