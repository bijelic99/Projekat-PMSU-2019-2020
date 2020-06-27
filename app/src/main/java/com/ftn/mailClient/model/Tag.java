package com.ftn.mailClient.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"color", "textColor"})
public class Tag extends Identifiable {
    private  String name;
    private  String color;
    private  String textColor;

    @Ignore
    public Tag(){
        color = "white";
        textColor = "black";
    }

    public Tag(Long id, String name) {
        super(id);
        this.name = name;
        this.color = "white";
        this.textColor = "black";
    }

    @Ignore
    public Tag(Long id, String name, String color, String textColor) {
        super(id);
        this.name = name;
        this.color = color;
        this.textColor = textColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name != null ? name : "";
    }
}
