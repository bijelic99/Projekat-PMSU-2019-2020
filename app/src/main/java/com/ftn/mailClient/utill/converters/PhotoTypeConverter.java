package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Photo;
import org.jetbrains.annotations.NotNull;


public class PhotoTypeConverter {
    @TypeConverter
    public Photo fromJson(String json){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(json, Photo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public String toJson(Photo photo){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(photo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
