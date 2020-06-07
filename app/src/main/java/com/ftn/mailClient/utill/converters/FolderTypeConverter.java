package com.ftn.mailClient.utill.converters;

import android.util.Log;
import androidx.room.TypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Photo;

public interface FolderTypeConverter {
    @TypeConverter
    static FolderMetadata fromString(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, FolderMetadata.class);
        } catch (JsonProcessingException e) {
            Log.e("ConverterError", e.getMessage());
            return null;
        }
    }

    @TypeConverter
    static String toString(FolderMetadata folder){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(folder);
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "null";
        }

    }
}
