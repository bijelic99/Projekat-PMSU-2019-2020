package com.ftn.mailClient.utill.converters;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Attachment;
import com.ftn.mailClient.model.Contact;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface AttachmentListTypeConverter {
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static Set<Attachment> fromJsonString(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Long[] ids = objectMapper.readValue(s, Long[].class);

            return Arrays.stream(ids).map(aLong -> {
                Attachment attachment = new Attachment();
                attachment.setId(aLong);
                return attachment;
            }).collect(Collectors.toSet());
        }
        catch (Exception e ){
            Log.e("ConverterError", e.getMessage());
            return new HashSet<>();
        }

    }
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static String toJsonString(Set<Attachment> attachments){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(attachments.stream().mapToLong(value -> value.getId()).toArray());
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "[]";
        }

    }
}
