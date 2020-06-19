package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Tag;

import java.util.List;

@Dao
public interface TagDao extends DaoInterface<Tag> {

    @Query("SELECT * FROM Tag WHERE ID = :id")
    LiveData<Tag> getLiveDataTagById(Long id);

    @Query("SELECT * FROM TAG WHERE ID IN(:ids)")
    public LiveData<List<Tag>> getTags(List<Long> ids);

    @Query("SELECT * FROM Tag")
    public LiveData<List<Tag>> getTags();

    @Query("Delete from Tag where id = :id")
    void deleteTag(Long id);

    @Query("Select * from tag")
    LiveData<List<Tag>> getAllTags();

    @Query("Select * from tag")
    List<Tag> getAllTagsNonLive();
}
