package com.ftn.mailClient.model;

import androidx.annotation.NonNull;

public enum Condition {
    TO("To"), FROM("From"), CC("CC"), SUBJECT("Subject");

    private String name;
    Condition(String name){
        this.name = name;
    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
