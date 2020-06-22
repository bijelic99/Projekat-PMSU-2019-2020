package com.ftn.mailClient.model;

import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.ftn.mailClient.utill.converters.PhotoTypeConverter;

public class ContactMetadata extends Identifiable{
    private String displayName;
    private String email;
    @TypeConverters(PhotoTypeConverter.class)
    private Photo photo;

    @Ignore
    public ContactMetadata(){

    }

    public ContactMetadata(Long id, String displayName, String email, Photo photo){
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.photo = photo;
    }

    @Ignore
    public ContactMetadata(Contact contact){
        id = contact.getId();
        displayName = contact.getDisplayName();
        email = contact.getEmail();
        photo = contact.getPhoto();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
