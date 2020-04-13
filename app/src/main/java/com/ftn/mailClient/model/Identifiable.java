package com.ftn.mailClient.model;

public abstract class Identifiable {
    protected int id;

    public Identifiable() {
        this(null);
    }

    private Identifiable(int id){
        this.id = id
    }

    public Identifiable(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
