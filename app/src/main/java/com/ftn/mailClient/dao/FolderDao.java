package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.linkingClasses.FolderInnerFolders;
import com.ftn.mailClient.model.linkingClasses.FolderMessage;
import com.ftn.mailClient.model.linkingClasses.MessageMetadata;

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

    @Query("Select f.id as id, f.name as name, ifnull(x.numberOfMessages, 0) as numberOfMessages, ifnull(y.numberOfFolders, 0) as numberOfFolders from FolderInnerFolders fif join Folder f on fif.childFolderId = f.id left join (Select folderId, count(messageId) as numberOfMessages from foldermessage group by folderId) x on f.id = x.folderId left join (select parentFolderId, count(childFolderId) as numberOfFolders from folderinnerfolders group by parentFolderId) y on f.id = y.parentFolderId where fif.parentFolderId = :id")
    LiveData<List<FolderMetadata>> getFolders(Long id);

    @Query("Select f.id as id, f.name as name, ifnull(x.numberOfMessages, 0) as numberOfMessages, ifnull(y.numberOfFolders, 0) as numberOfFolders from FolderInnerFolders fif join Folder f on fif.childFolderId = f.id left join (Select folderId, count(messageId) as numberOfMessages from foldermessage group by folderId) x on f.id = x.folderId left join (select parentFolderId, count(childFolderId) as numberOfFolders from folderinnerfolders group by parentFolderId) y on f.id = y.parentFolderId where fif.parentFolderId = :id")
    List<FolderMetadata> getFoldersNonLive(Long id);

    @Query("Select m.id, m.account, m._from, m._to, m.cc, m.bcc, m.dateTime, m.subject, m.content, m.attachments, m.tags, m.unread from FolderMessage fm join message m on fm.messageId = m.id where fm.folderId = :id")
    LiveData<List<Message>> getMessages(Long id);

    @Query("Select m.id, m.account, m._from, m._to, m.cc, m.bcc, m.dateTime, m.subject, m.content, m.attachments, m.tags, m.unread from FolderMessage fm join message m on fm.messageId = m.id where fm.folderId = :id")
    List<Message> getMessagesNonLive(Long id);



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMessagesToFolder(List<FolderMessage> folderMessages);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMessagesToFolder(FolderMessage... folderMessages);

    @Query("Select f.id as id, f.name as name, ifnull(x.numberOfMessages, 0) as numberOfMessages, ifnull(y.numberOfFolders, 0) as numberOfFolders from Folder f left join (Select folderId, count(messageId) as numberOfMessages from foldermessage group by folderId) x on f.id = x.folderId left join (Select parentFolderId, count(childFolderId) as numberOfFolders from folderinnerfolders group by parentFolderId) y on f.id = y.parentFolderId where f.id = :id")
    LiveData<FolderMetadata> getFolderMetadataById(Long id);

    @Query("Select f.id as id, f.name as name, ifnull(x.numberOfMessages, 0) as numberOfMessages, ifnull(y.numberOfFolders, 0) as numberOfFolders from Folder f left join (Select folderId, count(messageId) as numberOfMessages from foldermessage group by folderId) x on f.id = x.folderId left join (Select parentFolderId, count(childFolderId) as numberOfFolders from folderinnerfolders group by parentFolderId) y on f.id = y.parentFolderId where f.id = :id")
    FolderMetadata getFolderMetadataByIdNonLive(Long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFoldersToFolder(List<FolderInnerFolders> innerFolders);

    @Query("Delete from FolderInnerFolders where parentFolderId = :id")
    void deleteAllFolderChlildrenFolders(Long id);

    @Query("Delete from FolderMessage where folderId = :id")
    void deleteAllFolderMessages(Long id);

    @Query("Select f.id from AccountFolder af join Folder f on af.folderId = f.id where af.accountId = :accountId and f.name = 'Sent'")
    Long getSentFolderId(Long accountId);

    @Query("Select f.id from AccountFolder af join Folder f on af.folderId = f.id where af.accountId = :accountId and f.name = 'Drafts'")
    Long getDraftsFolderId(Long accountId);

}
