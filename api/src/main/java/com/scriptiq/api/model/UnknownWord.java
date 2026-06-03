package com.scriptiq.api.model;

import lombok.Data;

@Data
public class UnknownWord {

    private final String word;

    private int count;

    private String prediction;

    private boolean approved;
}