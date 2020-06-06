package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Contact;

public interface ContactTypeConverter {
    @TypeConverter
    static Contact fromLong(Long id){
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }

    @TypeConverter
    static Long toLong(Contact contact){
        return contact != null ? contact.getId() : null;
    }
}

