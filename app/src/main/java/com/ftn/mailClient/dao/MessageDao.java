package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.MessageMetadata;

import java.util.List;

@Dao
public interface MessageDao extends DaoInterface<Message> {
    @Query("SELECT * FROM MESSAGE WHERE ID IN(:ids)")
    public LiveData<List<Message>> getMessages(List<Long> ids);

    @Query("SELECT * FROM MESSAGE WHERE ID IN(:ids)")
    public List<Message> getCurrentMessages(List<Long> ids);

    @Query("SELECT ID FROM MESSAGE WHERE ID IN(:ids)")
    public List<Long> getIdsInList(List<Long> ids);

    @Query("SELECT * FROM MESSAGE WHERE ID = :id")
    LiveData<Message> getLiveDataMessageById(Long id);

    @Query("SELECT * FROM MESSAGE WHERE ID = :id")
    Message getMessageById(Long id);

//    @Query("Select m.id as id, m.subject as subject, m.content as content, m.dateTime as dateTime from message m")
//    LiveData<MessageMetadata> getMessageMetadataById(Long id);
}
