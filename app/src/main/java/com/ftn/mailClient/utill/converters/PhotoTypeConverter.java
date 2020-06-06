package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Photo;
import org.jetbrains.annotations.NotNull;


public interface PhotoTypeConverter {
    @TypeConverter
    static Photo fromLong(Long id){
        Photo p = new Photo();
        p.setId(id);
        return p;
    }

    @TypeConverter
    static Long toLong(Photo photo){
        return photo != null ? photo.getId() : null;
    }
}
