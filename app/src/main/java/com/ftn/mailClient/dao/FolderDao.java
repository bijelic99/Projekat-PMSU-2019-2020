package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.linkingClasses.FolderMessage;

import java.util.List;

@Dao
public interface FolderDao extends DaoInterface<Folder> {
    @Query("SELECT * FROM FOLDER ORDER BY ID DESC")
    LiveData<List<Folder>> getAllFolders();

    @Query("Select * from folder")
    List<Folder> getAllFolderNonLive();

    @Query("SELECT * FROM FOLDER WHERE ID = :id")
    LiveData<Folder> getLiveDataFolderById(Long id);

    @Query("SELECT * FROM FOLDER WHERE ID = :id")
    Folder getFolderById(Long id);

    @Query("SELECT * FROM FOLDER WHERE ID IN(:ids)")
    LiveData<Folder> getFolders(List<Long> ids);

    @Query("Select m.id, m.account, m._from, m._to, m.cc, m.bcc, m.dateTime, m.subject, m.content, m.attachments, m.tags, m.unread from FolderMessage fm join message m on fm.messageId = m.id where fm.folderId = :id")
    LiveData<List<Message>> getMessages(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessagesToFolder(List<FolderMessage> folderMessages);

}
