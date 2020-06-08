package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.database.Observable;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.repository.AccountRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;
    private Long accountId;
    private LiveData<Account> account;
    private LiveData<List<FolderMetadata>> accountFolders;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
    }

    public LiveData<Account> getAccount() {
        return account;
    }

    public void setAccount(Long id) {
        accountId = id;
        this.account = accountRepository.getById(id);
    }

    public LiveData<List<FolderMetadata>> getAccountFolders(){
        if(accountFolders == null) accountFolders = accountRepository.getAccountFolders(accountId);
        return accountFolders;
    }

    public LiveData<FetchStatus> syncAccountFolders(){
        return accountRepository.syncAccountFolders(accountId);
    }

}
