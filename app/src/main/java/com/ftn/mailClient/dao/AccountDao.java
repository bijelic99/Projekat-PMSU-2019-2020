package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.utill.AccountFoldersWrapper;
import com.ftn.mailClient.utill.converters.FolderListTypeConverter;
import com.ftn.mailClient.utill.converters.FolderTypeConverter;

import java.util.List;
import java.util.Set;

@Dao
public interface AccountDao extends DaoInterface<Account> {

    @TypeConverters(FolderListTypeConverter.CursorToFolderList.class)
    @Query("SELECT accountFolders FROM Account WHERE id = :id")
    AccountFoldersWrapper getAccountFolders(Long id);

    @TypeConverters(FolderListTypeConverter.CursorToFolderList.class)
    @Query("SELECT accountFolders FROM Account WHERE id = :id")
    LiveData<AccountFoldersWrapper> getLiveDataAccountFolders(Long id);

    @Query("SELECT * FROM Account WHERE ID = :id")
    LiveData<Account> getLiveDataAccountById(Long id);

    @Query("SELECT * FROM Account WHERE ID = :id")
    Account getAccountById(Long id);
}
