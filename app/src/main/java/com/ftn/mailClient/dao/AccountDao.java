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


    @Query("Select f.id as id, f.name as name, count(fm.messageId) as numberOfMessages, count(fif.childFolderId) as numberOfFolders from AccountFolder af join folder f on af.folderId = f.id left join foldermessage fm on f.id = fm.folderId left join folderinnerfolders fif on f.id = fif.parentFolderId where af.accountId = :id group by f.id")
    LiveData<List<FolderMetadata>> getAccountFolders(Long id);

    @Query("Select f.id, f.name, f.parentFolder from AccountFolder af join folder f on af.folderId = f.id where af.accountId = :id group by f.id")
    List<Folder> getAccountFoldersNoLive(Long id);

    @Query("delete from accountfolder where accountId = :id")
    void deleteAccountFolders(Long id);
}
