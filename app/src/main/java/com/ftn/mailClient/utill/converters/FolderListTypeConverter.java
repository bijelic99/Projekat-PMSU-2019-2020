package com.ftn.mailClient.utill.converters;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface FolderListTypeConverter {
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static Set<FolderMetadata> fromJsonString(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            FolderMetadata[] folders = objectMapper.readValue(s, FolderMetadata[].class);
            return new HashSet<>(Arrays.asList(folders));
        }
        catch (Exception e ){
            Log.e("ConverterError", e.getMessage());
            return new HashSet<>();
        }

    }
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    static String toJsonString(Set<FolderMetadata> folders){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(folders);
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "[]";
        }

    }
}
