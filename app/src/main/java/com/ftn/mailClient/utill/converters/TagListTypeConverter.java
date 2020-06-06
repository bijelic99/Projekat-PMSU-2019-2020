package com.ftn.mailClient.utill.converters;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Attachment;
import com.ftn.mailClient.model.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface TagListTypeConverter {
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static Set<Tag> fromJsonString(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Long[] ids = objectMapper.readValue(s, Long[].class);

            return Arrays.stream(ids).map(aLong -> {
                Tag tag = new Tag();
                tag.setId(aLong);
                return tag;
            }).collect(Collectors.toSet());
        }
        catch (Exception e ){
            Log.e("ConverterError", e.getMessage());
            return new HashSet<>();
        }

    }
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static String toJsonString(Set<Tag> tags){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(tags.stream().mapToLong(value -> value.getId()).toArray());
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "[]";
        }

    }
}
