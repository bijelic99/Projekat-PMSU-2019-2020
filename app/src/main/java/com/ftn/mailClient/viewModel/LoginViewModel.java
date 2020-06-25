package com.ftn.mailClient.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.utill.enums.FetchStatus;

public class LoginViewModel extends AndroidViewModel {
    private String username;
    private String password;

    private User

    public LoginViewModel(@NonNull Application application) {
        super(application);

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LiveData<FetchStatus> login(){

    }
}
