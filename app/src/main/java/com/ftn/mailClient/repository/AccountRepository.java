package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.lifecycle.*;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.asyncTasks.AccountAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class AccountRepository extends Repository<Account, AccountDao> {
    public AccountRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.accountDao();
    }

    @Override
    public LiveData<FetchStatus> insert(Account value) {
        return null;
    }

    @Override
    public void update(Account value) {

    }

    @Override
    public void delete(Account value) {

    }

    @Override
    public LiveData<List<Account>> getAll() {
        return dao.getAccounts();
    }

    @Override
    public LiveData<Account> getById(Long id) {
        LiveData<Account> accountLiveData = dao.getLiveDataAccountById(id);
        if (accountLiveData.getValue() == null) fetchAccount(id);

        return accountLiveData;
    }

    @Override
    public LiveData<List<Account>> getByIdList(List<Long> ids) {
        return null;
    }

    public void fetchAccount(Long id) {
        new AccountAsyncTasks.FetchAccountAsyncTask(database).execute(id);
    }

    public LiveData<List<FolderMetadata>> getAccountFolders(Long id) {
        return dao.getAccountFolders(id);
    }

    public LiveData<FetchStatus> syncAccountFolders(Long accountId) {
        MutableLiveData<FetchStatus> fetchStatus = new MutableLiveData<>(FetchStatus.FETCHING);
        new AccountAsyncTasks.FetchAccountFoldersAsyncTask(database,
                isSuccess -> {
                    if (isSuccess) fetchStatus.setValue(FetchStatus.DONE);
                    else fetchStatus.setValue(FetchStatus.ERROR);
                }).execute(accountId);
        return fetchStatus;
    }

    public LiveData<List<Message>> getAccountMessages() {
        return dao.getAccountMessages();
    }

    public LiveData<List<Tag>> getAccountTags() {
        return dao.getAccountTags();
    }

    public LiveData<FetchStatus> fetchAccountMessages(Long accountId) {
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new AccountAsyncTasks.FetchAccountMessagesAsyncTask(database, value -> fetchStatusMutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR))
                .execute(accountId);
        return fetchStatusMutableLiveData;
    }

    public LiveData<FetchStatus> fetchAccountTags(Long accountID) {
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new AccountAsyncTasks.FetchAccountTagsAsyncTask(database, x -> fetchStatusMutableLiveData.setValue(x ? FetchStatus.DONE : FetchStatus.ERROR))
                .execute(accountID);
        return fetchStatusMutableLiveData;
    }

    public LiveData<FetchStatus> insertNewAccount(Account account, Long userId) {
        MutableLiveData<FetchStatus> mutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new AccountAsyncTasks.AddNewUserAccountAsyncTask(database, value -> mutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR), userId).execute(account);
        return mutableLiveData;
    }

    public LiveData<FetchStatus> changeAccount(Long userId, Long accountId) {
        MutableLiveData<FetchStatus> mutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new AccountAsyncTasks.ChangeAccountAsyncTask(database, value -> mutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR), userId, accountId).execute();
        return mutableLiveData;
    }
}
