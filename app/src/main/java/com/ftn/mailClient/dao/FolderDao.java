package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Query;
import com.ftn.mailClient.model.Folder;

import java.util.List;

public interface FolderDao extends DaoInterface<Folder> {
    @Query("SELECT * FROM FOLDER ORDER BY ID DESC")
    LiveData<List<Folder>> getAllFolders();

    @Query("SELECT * FROM FOLDER WHERE ID = :id")
    LiveData<Folder> getLiveDataFolderById(Long id);

    @Query("SELECT * FROM FOLDER WHERE ID = :id")
    Folder getFolderById(Long id);

    @Query("SELECT * FROM FOLDER WHERE ID IN(:ids)")
    LiveData<Folder> getFolders(List<Long> ids);
}
