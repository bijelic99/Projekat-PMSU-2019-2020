package com.ftn.mailClient.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ftn.mailClient.utill.converters.PhotoTypeConverter;

@Entity
public class Contact extends Identifiable {
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    @TypeConverters(PhotoTypeConverter.class)
    private Photo photo;
    private String format;

    public Contact(Long id, String firstName, String lastName, String displayName, String email, Photo photo, String format) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.email = email;
        this.photo = photo;
        this.format = format;
    }

    @Ignore
    public Contact() {
        this(null, null, null, null, null, null, null);
    }

    @JsonProperty("first")
    public String getFirstName() {
        return firstName;
    }

    @JsonGetter("first")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last")
    public String getLastName() {
        return lastName;
    }

    @JsonGetter("last")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("display")
    public String getDisplayName() {
        return displayName;
    }

    @JsonGetter("display")
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @NonNull
    @Override
    public String toString() {
        return ((displayName != null) && !displayName.isEmpty()) ? displayName : (((lastName != null) && !lastName.isEmpty() && (firstName != null) && !firstName.isEmpty()) ? (firstName + " " + lastName) : (((lastName != null) && !lastName.isEmpty()) ? lastName : (((firstName != null) && !firstName.isEmpty()) ? firstName : (((email != null) && !email.isEmpty()) ? email : ""))));
    }
}
