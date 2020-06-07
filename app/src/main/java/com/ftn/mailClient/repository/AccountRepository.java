package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.repository.asyncTasks.AccountAsyncTasks;
import com.ftn.mailClient.utill.AccountFoldersWrapper;

import java.util.List;

public class AccountRepository extends Repository<Account, AccountDao> {
    public AccountRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.accountDao();
    }

    @Override
    public void insert(Account value) {

    }

    @Override
    public void update(Account value) {

    }

    @Override
    public void delete(Account value) {

    }

    @Override
    public LiveData<List<Account>> getAll() {
        return null;
    }

    @Override
    public LiveData<Account> getById(Long id) {
        LiveData<Account> accountLiveData = dao.getLiveDataAccountById(id);
        if(accountLiveData.getValue() == null) fetchAccount(id);

        return accountLiveData;
    }

    @Override
    public LiveData<List<Account>> getByIdList(List<Long> ids) {
        return null;
    }

    public void fetchAccount(Long id){
        new AccountAsyncTasks.FetchAccountAsyncTask(database).execute(id);
    }

    public LiveData<List<FolderMetadata>> getAccountFolders(Long id){
        final LiveData<AccountFoldersWrapper> accountFoldersWrapperLiveData = dao.getLiveDataAccountFolders(id);
        MutableLiveData<List<FolderMetadata>> listLiveData = new MutableLiveData<List<FolderMetadata>>(null);
        accountFoldersWrapperLiveData.observeForever(new Observer<AccountFoldersWrapper>() {
            @Override
            public void onChanged(AccountFoldersWrapper accountFoldersWrapper) {
                if(accountFoldersWrapper != null) listLiveData.postValue(accountFoldersWrapper.getFolders());
            }
        });


        return (LiveData<List<FolderMetadata>>) listLiveData;
    }
}
