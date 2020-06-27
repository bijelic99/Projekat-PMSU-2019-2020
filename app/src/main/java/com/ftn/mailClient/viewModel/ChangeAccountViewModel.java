package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.R;
import com.ftn.mailClient.authorization.UserAccountInfo;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.repository.AccountRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class ChangeAccountViewModel extends AndroidViewModel {
    private Long currentAccountId;
    private Long userId;

    private UserAccountInfo userAccountInfo;
    private AccountRepository accountRepository;
    private LiveData<List<Account>> accountLiveData;

    public ChangeAccountViewModel(@NonNull Application application) {
        super(application);
        userAccountInfo = UserAccountInfo.getUserAccountInfo();
        if(userAccountInfo.getLoggedIn()){
            currentAccountId = userAccountInfo.getSelectedAccountId();
            userId = userAccountInfo.getUserId();
        }
        accountRepository = new AccountRepository(application);
        accountLiveData = accountRepository.getAll();
    }

    public void setCurrentAccountId(Long currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    public LiveData<List<Account>> getAccountLiveData() {
        return accountLiveData;
    }

    public Long getCurrentAccountId() {
        return currentAccountId;
    }

    public LiveData<FetchStatus> chooseNewAccount(){
        if(userId != null && currentAccountId != null) {
            LiveData<FetchStatus> liveData = accountRepository.changeAccount(userId, currentAccountId);
            liveData.observeForever(new Observer<FetchStatus>() {
                @Override
                public void onChanged(FetchStatus fetchStatus) {
                    if (fetchStatus.equals(FetchStatus.ERROR) || fetchStatus.equals(FetchStatus.DONE)) {
                        if (fetchStatus.equals(FetchStatus.DONE)) {
                            userAccountInfo.setSelectedAccountId(currentAccountId, getApplication());
                            Toast.makeText(getApplication(), R.string.account_change_success, Toast.LENGTH_SHORT).show();
                        }
                        if (fetchStatus.equals(FetchStatus.ERROR))
                            Toast.makeText(getApplication(), R.string.change_account_error, Toast.LENGTH_SHORT).show();
                        liveData.removeObserver(this);
                    }
                }
            });
            return liveData;
        }
        else Toast.makeText(getApplication(), R.string.change_account_error, Toast.LENGTH_SHORT).show();
        return null;
    }
}
