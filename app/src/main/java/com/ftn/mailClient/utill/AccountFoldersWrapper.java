package com.ftn.mailClient.utill;

import androidx.room.Entity;
import androidx.room.TypeConverters;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.utill.converters.FolderListTypeConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountFoldersWrapper {
    @TypeConverters(FolderListTypeConverter.class)
    private List<FolderMetadata> folders;

    public AccountFoldersWrapper(List<FolderMetadata> folders) {
        this.folders = folders;
    }

    public AccountFoldersWrapper() {
        this.folders = new ArrayList<>();
    }

    public List<FolderMetadata> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderMetadata> folders) {
        this.folders = folders;
    }
}
