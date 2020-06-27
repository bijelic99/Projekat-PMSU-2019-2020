package com.ftn.mailClient.model;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity
public class Photo extends Identifiable {
    private String path;

    public Photo(Long id, String path) {
        super(id);
        this.path = path;
    }

    @Ignore
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
