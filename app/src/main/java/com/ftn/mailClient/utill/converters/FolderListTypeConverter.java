package com.ftn.mailClient.utill.converters;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.utill.AccountFoldersWrapper;

import java.util.*;
import java.util.stream.Collectors;

public class FolderListTypeConverter {
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<FolderMetadata> fromJsonString(String s){
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            FolderMetadata[] folders = objectMapper.readValue(s, FolderMetadata[].class);
            return new ArrayList<>(Arrays.asList(folders));
        }
        catch (Exception e ){
            Log.e("ConverterError", e.getMessage());
            return new ArrayList<>();
        }

    }
    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String toJsonString(List<FolderMetadata> folders){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(folders);
        }catch (Exception e){
            Log.e("ConverterError", e.getMessage());
            return "[]";
        }

    }



    public static class CursorToFolderList{
        @RequiresApi(api = Build.VERSION_CODES.N)
        @TypeConverter
        public AccountFoldersWrapper fromJson(String json){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                FolderMetadata[] folderMetadata = objectMapper.readValue(json, FolderMetadata[].class);
                return new AccountFoldersWrapper(Arrays.asList(folderMetadata));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new AccountFoldersWrapper();
            }



        }

    }
}
