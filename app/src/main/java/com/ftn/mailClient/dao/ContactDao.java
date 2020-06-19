package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.ftn.mailClient.model.Contact;

import java.util.List;

@Dao
public interface ContactDao extends DaoInterface<Contact> {
    @Query("Select * from contact")
    LiveData<List<Contact>> getAllContacts();

    @Query("Select * from contact")
    List<Contact> getAllContactsNonLive();

    @Query("Select * from contact where lower(IFNULL(displayName,'')) like ('%' || lower(IFNULL(:term,'')) || '%') or lower(IFNULL(email,'')) like ('%' || lower(IFNULL(:term,'')) || '%') or lower(IFNULL(firstname, '')) like ('%' || lower(IFNULL(:term,'')) || '%') or lower(IFNULL(lastName,'')) like ('%' || lower(IFNULL(:term,'')) || '%')")
    LiveData<List<Contact>> getContacts(String term);

}
