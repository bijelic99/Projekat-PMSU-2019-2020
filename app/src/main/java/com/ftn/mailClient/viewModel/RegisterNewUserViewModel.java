package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.UserRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

public class RegisterNewUserViewModel extends AndroidViewModel {
    private User user;
    private UserRepository userRepository;

    public RegisterNewUserViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        userRepository = new UserRepository(application);
        user = new User();
    }

    public void setUserUsername(String username){
        user.setUsername(username);
    }
    public void setUserPassword(String password){
        user.setPassword(password);
    }
    public void setUserFirstName(String firstName){
        user.setFirstName(firstName);
    }
    public void setUserLastName(String lastName){
        user.setLastName(lastName);
    }

    public void resetUser(){
        user = new User();
    }

    public void insertNewUser(){
        LiveData<FetchStatus> liveData =  userRepository.insert(user);
        liveData.observeForever(new Observer<FetchStatus>() {
            @Override
            public void onChanged(FetchStatus fetchStatus) {
                if(fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)){
                    if(fetchStatus.equals(FetchStatus.DONE)) Toast.makeText(getApplication(), R.string.registered, Toast.LENGTH_SHORT).show();
                    if(fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(getApplication(), R.string.failed_to_register, Toast.LENGTH_SHORT).show();
                    liveData.removeObserver(this);
                }
            }
        });
    }
}
