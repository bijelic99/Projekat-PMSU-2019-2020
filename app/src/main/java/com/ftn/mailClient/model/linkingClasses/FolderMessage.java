package com.ftn.mailClient.model.linkingClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;

@Entity(primaryKeys = {"folderId", "messageId"}, foreignKeys = {
        @ForeignKey(entity = Folder.class, parentColumns = "id", childColumns = "folderId"),
        @ForeignKey(entity = Message.class, parentColumns = "id", childColumns = "messageId"),
} )
public class FolderMessage {
    @NonNull
    @ColumnInfo(index = true)
    private Long folderId;
    @NonNull
    @ColumnInfo(index = true)
    private Long messageId;

    public FolderMessage(Long folderId, Long messageId) {
        this.folderId = folderId;
        this.messageId = messageId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
