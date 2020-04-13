package com.ftn.mailClient.model;

import android.util.Base64;

public class Attachment extends Identifiable {
    private String base64Data;
    private String type;
    private String name;
    public Attachment(){
        this(null,null,null,null);
    }
    public Attachment(int id, String base64Data, String type, String name) {
        super(id);
        this.base64Data = base64Data;
        this.type = type;
        this.name = name;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
