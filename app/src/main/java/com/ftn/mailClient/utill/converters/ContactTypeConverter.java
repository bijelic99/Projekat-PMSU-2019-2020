package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Contact;

public class ContactTypeConverter {
    @TypeConverter
    public Contact fromJson(String json){
        if(json != null) {
            ObjectMapper om = new ObjectMapper();
            Contact c = null;
            try {
                c = om.readValue(json, Contact.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return c;
        }
        else return null;
    }

    @TypeConverter
    public String toJson(Contact contact){
        ObjectMapper om = new ObjectMapper();

        try {
            return contact != null ? om.writeValueAsString(contact) : null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

