package com.ftn.mailClient.model.linkingClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.ftn.mailClient.model.Folder;

@Entity(foreignKeys = {
        @ForeignKey(entity = Folder.class, parentColumns = "id", childColumns = "parentFolderId"),
        @ForeignKey(entity = Folder.class, parentColumns = "id", childColumns = "childFolderId")
})
public class FolderInnerFolders {
    @NonNull
    @ColumnInfo(index = true)
    private Long parentFolderId;
    @NonNull
    @PrimaryKey
    @ColumnInfo(index = true)
    private Long childFolderId;

    public FolderInnerFolders(Long parentFolderId, Long childFolderId) {
        this.parentFolderId = parentFolderId;
        this.childFolderId = childFolderId;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public Long getChildFolderId() {
        return childFolderId;
    }

    public void setChildFolderId(Long childFolderId) {
        this.childFolderId = childFolderId;
    }
}
