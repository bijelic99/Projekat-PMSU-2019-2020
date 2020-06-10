package com.ftn.mailClient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

public abstract class Identifiable implements Serializable {
    protected Long id;

    public Identifiable() {
        this(null);
    }

    public Identifiable(Long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }
}
