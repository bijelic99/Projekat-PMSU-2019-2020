package com.ftn.mailClient.model;

public class Tag extends Identifiable {
    private  String name;
    private  String color;
    private  String textColor;

    public Tag(Integer id, String name) {
        super(id);
        this.name = name;
        this.color = "white";
        this.textColor = "black";
    }

    public Tag(Integer id, String name, String color, String textColor) {
        super(id);
        this.name = name;
        this.color = color;
        this.textColor = textColor;
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
}
