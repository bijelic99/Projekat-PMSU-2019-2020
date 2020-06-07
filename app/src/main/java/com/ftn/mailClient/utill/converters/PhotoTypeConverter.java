package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Photo;
import org.jetbrains.annotations.NotNull;


public class PhotoTypeConverter {
    @TypeConverter
    public static Photo fromLong(Long id){
        Photo p = new Photo();
        p.setId(id);
        return p;
    }

    @TypeConverter
    public static Long toLong(Photo photo){
        return photo != null ? photo.getId() : null;
    }
}
