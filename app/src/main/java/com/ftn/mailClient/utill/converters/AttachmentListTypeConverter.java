package com.ftn.mailClient.utill.converters;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Attachment;
import com.ftn.mailClient.model.Contact;

import java.util.*;
import java.util.stream.Collectors;

public class AttachmentListTypeConverter {
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Attachment> fromJsonString(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Attachment[] attachments = objectMapper.readValue(s, Attachment[].class);

            return Arrays.stream(attachments).collect(Collectors.toList());
        }
        catch (Exception e ){
            Log.e("ConverterError", e.getMessage());
            return new ArrayList<>();
        }

    }
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String toJsonString(List<Attachment> attachments){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(attachments.toArray());
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "[]";
        }

    }
}
