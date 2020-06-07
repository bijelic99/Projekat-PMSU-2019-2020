package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Query;
import androidx.room.TypeConverters;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.utill.converters.FolderListTypeConverter;

import java.util.List;
import java.util.Set;

public interface AccountDao extends DaoInterface<Account> {

    @TypeConverters(FolderListTypeConverter.class)
    @Query("SELECT accountFolders FROM Account WHERE id = :id")
    List<FolderMetadata> getAccountFolders(Long id);

    @TypeConverters(FolderListTypeConverter.class)
    @Query("SELECT accountFolders FROM Account WHERE id = :id")
    LiveData<List<FolderMetadata>> getLiveDataAccountFolders(Long id);

    @Query("SELECT * FROM Account WHERE ID = :id")
    LiveData<Account> getLiveDataAccountById(Long id);

    @Query("SELECT * FROM Account WHERE ID = :id")
    Account getAccountById(Long id);
}
