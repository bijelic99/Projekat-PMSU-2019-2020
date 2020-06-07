package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.ftn.mailClient.model.Message;

import java.util.List;

@Dao
public interface MessageDao extends DaoInterface<Message> {
    @Query("SELECT * FROM MESSAGE WHERE ID IN(:ids)")
    public LiveData<List<Message>> getMessages(List<Long> ids);

    @Query("SELECT * FROM MESSAGE WHERE ID IN(:ids)")
    public List<Message> getCurrentMessages(List<Long> ids);

    @Query("SELECT ID FROM MESSAGE WHERE ID IN(:ids)")
    public List<Long> getIdsInList(List<Long> ids);
}
