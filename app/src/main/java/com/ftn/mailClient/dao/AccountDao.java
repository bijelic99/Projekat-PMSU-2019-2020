package com.ftn.mailClient.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.linkingClasses.AccountFolder;

import java.util.List;

@Dao
public interface AccountDao extends DaoInterface<Account> {

    @Query("SELECT * FROM Account WHERE ID = :id")
    LiveData<Account> getLiveDataAccountById(Long id);

    @Query("SELECT * FROM Account WHERE ID = :id")
    Account getAccountById(Long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAccountFolders(List<AccountFolder> list);


    @Query("Select f.id as id, f.name as name, ifnull(x.numberOfMessages, 0) as numberOfMessages, ifnull(y.numberOfFolders, 0) as numberOfFolders from Folder f join accountfolder af on af.folderId = f.id left join (Select folderId, count(*) as numberOfMessages from foldermessage group by folderId) x on f.id = x.folderId left join (Select parentFolderId, count(*) as numberOfFolders from folderinnerfolders group by parentFolderId) y on f.id = y.parentFolderId where af.accountId = :id group by f.id")
    LiveData<List<FolderMetadata>> getAccountFolders(Long id);
}
