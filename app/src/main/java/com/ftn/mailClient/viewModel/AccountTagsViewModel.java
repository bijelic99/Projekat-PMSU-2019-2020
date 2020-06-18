package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.AccountRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.ArrayList;
import java.util.List;

public class AccountTagsViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;
    private LiveData<List<Tag>> tags;
    private Long accountId;

    public AccountTagsViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(application.getString(R.string.user_account_id))){
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        }
        else Toast.makeText(application, R.string.need_to_choose_an_account, Toast.LENGTH_LONG).show();
    }

    public LiveData<List<Tag>> getTags() {

        if(accountId != null && tags == null) tags = accountRepository.getAccountTags();
        else if(accountId == null) tags = new MutableLiveData<>(new ArrayList<>());
        return tags;
    }

    public LiveData<FetchStatus> syncTags(){
        if(accountId != null){
            return accountRepository.fetchAccountTags(accountId);
        }
        else return new MutableLiveData<>(FetchStatus.DONE);
    }
}
