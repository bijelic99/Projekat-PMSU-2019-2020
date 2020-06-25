package com.ftn.mailClient.repository;

import android.app.Application;
import android.os.Bundle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.UserDao;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.asyncTasks.UserAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class UserRepository extends Repository<User, UserDao> {

    public UserRepository(Application application) {
        super(application);
    }

    @Override
    public LiveData<FetchStatus> insert(User value) {
        return null;
    }

    @Override
    public void update(User value) {

    }

    @Override
    public void delete(User value) {

    }

    @Override
    public LiveData<List<User>> getAll() {
        return null;
    }

    @Override
    public LiveData<User> getById(Long id) {
        return null;
    }

    @Override
    public LiveData<List<User>> getByIdList(List<Long> ids) {
        return null;
    }

    public LiveData<Bundle> login(String username, String password) {
        Bundle statusUserBundle = new Bundle();
        statusUserBundle.putSerializable("status", FetchStatus.FETCHING);
        statusUserBundle.putSerializable("user", null);
        MutableLiveData<Bundle> fetchStatusMutableLiveData = new MutableLiveData<>(statusUserBundle);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        new UserAsyncTasks.LoginUserAsyncTask(database, value -> {
            Bundle bundle1 = fetchStatusMutableLiveData.getValue();
            if(value != null){
                bundle.putSerializable("status", FetchStatus.DONE);
                bundle.putSerializable("user", value.getUser());
                bundle.putString("token", value.getToken());
            }
            else {
                bundle.putSerializable("status", FetchStatus.ERROR);
                bundle.putSerializable("user", null);
                bundle.putString("user", null);
            }
        }).execute(bundle);
        return fetchStatusMutableLiveData;
    }
}
