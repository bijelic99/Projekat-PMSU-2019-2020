package com.ftn.mailClient.model;

public class Tag extends Identifiable {
    private  String name;

    public Tag(int id, String name) {
        super(id);
        this.name = name;
    }

    public  Tag(){
        this(null,null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
