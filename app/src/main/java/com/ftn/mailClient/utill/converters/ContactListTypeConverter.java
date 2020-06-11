package com.ftn.mailClient.utill.converters;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Message;

import java.util.*;
import java.util.stream.Collectors;

public class ContactListTypeConverter {

    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Contact> toList(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Long[] ids = objectMapper.readValue(s, Long[].class);

            return Arrays.stream(ids).map(aLong -> {
                Contact contact = new Contact();
                contact.setId(aLong);
                return contact;
            }).collect(Collectors.toList());
        }
        catch (Exception e ){
            Log.e("ConverterError", e.getMessage());
            return new ArrayList<>();
        }

    }
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String toJson(List<Contact> contacts){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(contacts.stream().mapToLong(value -> value.getId()).toArray());
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "[]";
        }

    }
}
