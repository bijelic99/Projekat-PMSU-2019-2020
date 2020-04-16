package com.ftn.mailClient.model;

public abstract class Identifiable {
    protected Integer id;

    public Identifiable() {
        this(null);
    }

    public Identifiable(Integer id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
