package com.ftn.mailClient.model;

public abstract class Identifiable {
    protected Long id;

    public Identifiable() {
        this(null);
    }

    public Identifiable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
