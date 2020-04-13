package com.ftn.mailClient.model;

public class Photo extends Identifiable {
    private String base64Photo;

    public Photo(int id, String base64Photo) {
        super(id);
        this.base64Photo = base64Photo;
    }
    public Photo() {
        super(null, null);
    }

    public String getBase64Photo() {
        return base64Photo;
    }

    public void setBase64Photo(String base64Photo) {
        this.base64Photo = base64Photo;
    }
}
