package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.R;
import com.ftn.mailClient.authorization.UserAccountInfo;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.UserRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

public class LoginViewModel extends AndroidViewModel {
    private String username;
    private String password;

    private UserRepository userRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LiveData<FetchStatus> login(){
        SharedPreferences sharedPreferences = getApplication()
                .getSharedPreferences(getApplication().getString(R.string.user_details_file_key), getApplication().MODE_PRIVATE);
        MutableLiveData<FetchStatus> mutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        LiveData<Bundle> data = userRepository.login(username, password);
        data.observeForever(new Observer<Bundle>() {
            @Override
            public void onChanged(Bundle bundle) {
                if(bundle != null){
                    FetchStatus fetchStatus = (FetchStatus) bundle.getSerializable("status");
                    mutableLiveData.setValue(fetchStatus);
                    if(fetchStatus.equals(FetchStatus.DONE)){
                        User u = (User) bundle.getSerializable("user");
                        String token = bundle.getString("token");

                        UserAccountInfo.getUserAccountInfo().login(token, u.getId(), getApplication());
                    }
                    mutableLiveData.setValue(fetchStatus);
                    if(fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)) data.removeObserver(this);

                }
            }
        });
        return mutableLiveData;
    }
}
