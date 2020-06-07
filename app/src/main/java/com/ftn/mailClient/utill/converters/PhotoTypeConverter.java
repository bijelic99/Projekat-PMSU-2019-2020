package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Photo;
import org.jetbrains.annotations.NotNull;


public class PhotoTypeConverter {
    @TypeConverter
    public Photo fromLong(Long id){
        Photo p = new Photo();
        p.setId(id);
        return p;
    }

    @TypeConverter
    public Long toLong(Photo photo){
        return photo != null ? photo.getId() : null;
    }
}
