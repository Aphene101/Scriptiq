package com.scriptiq.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnknownWord {

    private String word;

    private int count;

    private String prediction;

    private double confidence;

    private boolean approved;
}