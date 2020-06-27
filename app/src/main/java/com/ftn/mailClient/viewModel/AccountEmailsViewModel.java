package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.repository.AccountRepository;
import com.ftn.mailClient.utill.FilterEmail;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.ArrayList;
import java.util.List;

public class AccountEmailsViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;
    private LiveData<List<Message>> messages;
    private Long accountId;
    private MutableLiveData<FilterEmail> filterEmailMutableLiveData;

    public AccountEmailsViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(application.getString(R.string.user_account_id))){
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        }
        else Toast.makeText(application, R.string.need_to_choose_an_account, Toast.LENGTH_LONG).show();
        filterEmailMutableLiveData = new MutableLiveData<>(null);
    }

    public LiveData<List<Message>> getMessages() {

        if(accountId != null && messages == null) messages = accountRepository.getAccountMessages();
        else if(accountId == null) messages = new MutableLiveData<>(new ArrayList<>());
        return messages;
    }

    public LiveData<FetchStatus> syncMessages(){
        if(accountId != null){
            return accountRepository.fetchAccountMessages(accountId);
        }
        else return new MutableLiveData<>(FetchStatus.DONE);
    }

    public void setFilter(FilterEmail filter) {
        filterEmailMutableLiveData.setValue(filter);
    }

    public MutableLiveData<FilterEmail> getFilterEmailMutableLiveData() {
        return filterEmailMutableLiveData;
    }
}
