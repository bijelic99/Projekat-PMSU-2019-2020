package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.linkingClasses.FolderInnerFolders;
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

    @Query("Select f.id as id, f.name as name, count(fm.messageId) as numberOfMessages, count(fif1.childFolderId) as numberOfFolders from FolderInnerFolders fif join Folder f on fif.childFolderId = f.id join foldermessage fm on fif.childFolderId = fm.messageId join folderinnerfolders fif1 on fif.childFolderId = fif1.parentFolderId where fif.parentFolderId = :id group by fif.childFolderId")
    LiveData<List<FolderMetadata>> getFolders(Long id);

    @Query("Select f.id as id, f.name as name, count(fm.messageId) as numberOfMessages, count(fif1.childFolderId) as numberOfFolders from FolderInnerFolders fif join Folder f on fif.childFolderId = f.id join foldermessage fm on fif.childFolderId = fm.messageId join folderinnerfolders fif1 on fif.childFolderId = fif1.parentFolderId where fif.parentFolderId = :id group by fif.childFolderId")
    List<FolderMetadata> getFoldersNonLive(Long id);

    @Query("Select m.id, m.account, m._from, m._to, m.cc, m.bcc, m.dateTime, m.subject, m.content, m.attachments, m.tags, m.unread from FolderMessage fm join message m on fm.messageId = m.id where fm.folderId = :id")
    LiveData<List<Message>> getMessages(Long id);

    @Query("Select m.id, m.account, m._from, m._to, m.cc, m.bcc, m.dateTime, m.subject, m.content, m.attachments, m.tags, m.unread from FolderMessage fm join message m on fm.messageId = m.id where fm.folderId = :id")
    List<Message> getMessagesNonLive(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessagesToFolder(List<FolderMessage> folderMessages);

    @Query("Select f.id as id, f.name as name, count(fm.messageId) as numberOfMessages, count(fif.childFolderId) as numberOfFolders from Folder f left join foldermessage fm on f.id = fm.messageId left join folderinnerfolders fif on f.id = fif.parentFolderId where f.id = :id group by f.id")
    LiveData<FolderMetadata> getFolderMetadataById(Long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFoldersToFolder(List<FolderInnerFolders> innerFolders);

}
