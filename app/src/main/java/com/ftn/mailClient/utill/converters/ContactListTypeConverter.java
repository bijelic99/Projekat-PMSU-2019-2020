package com.ftn.mailClient.utill.converters;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Message;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface ContactListTypeConverter {
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static Set<Contact> fromJsonString(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Long[] ids = objectMapper.readValue(s, Long[].class);

            return Arrays.stream(ids).map(aLong -> {
                Contact contact = new Contact();
                contact.setId(aLong);
                return contact;
            }).collect(Collectors.toSet());
        }
        catch (Exception e ){
            Log.e("ConverterError", e.getMessage());
            return new HashSet<>();
        }

    }
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static String toJsonString(Set<Contact> contacts){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(contacts.stream().mapToLong(value -> value.getId()).toArray());
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "[]";
        }

    }
}
