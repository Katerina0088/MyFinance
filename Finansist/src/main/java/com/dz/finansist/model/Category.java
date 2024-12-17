package com.dz.finansist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)

public class Category {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }
    // Геттеры и сеттеры
}
