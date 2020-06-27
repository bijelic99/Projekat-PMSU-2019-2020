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
import com.ftn.mailClient.model.IncomingMailProtocol;
import com.ftn.mailClient.repository.AccountRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

public class AddAccountViewModel extends AndroidViewModel {
    AccountRepository accountRepository;

    private String smtpAddress;
    private IncomingMailProtocol incomingMailProtocol;
    private String incomingMailAddress;
    private String username;
    private String password;
    private Long userId;

    public AddAccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
        if(UserAccountInfo.getUserAccountInfo().getLoggedIn()) {
            userId = UserAccountInfo.getUserAccountInfo().getUserId();
        }
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    public void setIncomingMailProtocol(IncomingMailProtocol incomingMailProtocol) {
        this.incomingMailProtocol = incomingMailProtocol;
    }

    public void setIncomingMailAddress(String incomingMailAddress) {
        this.incomingMailAddress = incomingMailAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void reset(){
        this.smtpAddress = null;
        this.incomingMailProtocol = IncomingMailProtocol.POP3;
        this.incomingMailAddress = null;
        this.username = null;
        this.password = null;
    }

    public boolean validate(){
        if(smtpAddress == null) return false;
        if(incomingMailProtocol == null) return false;
        if(incomingMailAddress == null) return false;
        if(username == null) return false;
        if(password == null) return false;

        if(!smtpAddress.matches("^.+:\\d+$")) return false;
        if(!incomingMailAddress.matches("^.+:\\d+$")) return false;

        return true;
    }

    public void addAccount(){
        Account account = new Account();
        account.setUsername(this.username);
        account.setPassword(this.password);
        account.setSmtpAddress(this.smtpAddress);
        account.setIncomingMailProtocol(this.incomingMailProtocol);
        account.setIncomingMailAddress(this.incomingMailAddress);

        LiveData<FetchStatus> liveData = accountRepository.insertNewAccount(account, userId);
        liveData.observeForever(new Observer<FetchStatus>() {
            @Override
            public void onChanged(FetchStatus fetchStatus) {
                if(fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)){
                    if(fetchStatus.equals(FetchStatus.DONE)) Toast.makeText(getApplication(), R.string.accaount_add_successful, Toast.LENGTH_SHORT).show();
                    if(fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(getApplication(), R.string.account_add_error, Toast.LENGTH_SHORT).show();
                    liveData.removeObserver(this);
                }
            }
        });
    }
}
