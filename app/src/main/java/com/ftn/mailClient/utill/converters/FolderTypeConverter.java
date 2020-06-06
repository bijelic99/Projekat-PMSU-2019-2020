package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Photo;

public interface FolderTypeConverter {
    @TypeConverter
    static Folder fromLong(Long id){
        Folder f = new Folder();
        f.setId(id);
        return f;
    }

    @TypeConverter
    static Long toLong(Folder folder){
        return folder != null ? folder.getId() : null;
    }
}
