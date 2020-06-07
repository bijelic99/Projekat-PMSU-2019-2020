package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Contact;

public class ContactTypeConverter {
    @TypeConverter
    public Contact fromLong(Long id){
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }

    @TypeConverter
    public Long toLong(Contact contact){
        return contact != null ? contact.getId() : null;
    }
}

