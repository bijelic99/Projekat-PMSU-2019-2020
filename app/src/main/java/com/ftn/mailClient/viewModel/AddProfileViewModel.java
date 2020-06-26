package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.UserRepository;

public class AddProfileViewModel extends AndroidViewModel {
    private User user;
    private UserRepository userRepository;

    public AddProfileViewModel(@NonNull Application application) {
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
        userRepository.insert(user);
    }
}
