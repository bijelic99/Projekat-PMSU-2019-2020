package com.ftn.mailClient.model;

public class Photo extends Identifiable {
    private String path;

    public Photo(Long id, String path) {
        super(id);
        this.path = path;
    }
    public Photo() {
        super(null);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
