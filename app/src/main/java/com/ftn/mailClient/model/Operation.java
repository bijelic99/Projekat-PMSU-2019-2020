package com.ftn.mailClient.model;

import androidx.annotation.NonNull;

public enum Operation {
    MOVE("Move"), COPY("Copy"), DELETE("Delete");

    private String name;
    Operation(String name){
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
