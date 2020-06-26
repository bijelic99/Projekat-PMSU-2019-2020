package com.ftn.mailClient.repository;

import android.app.Application;
import android.os.Bundle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.UserDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.asyncTasks.UserAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class UserRepository extends Repository<User, UserDao> {

    public UserRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
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
                bundle1.putSerializable("status", FetchStatus.DONE);
                bundle1.putSerializable("user", value.getUser());
                bundle1.putString("token", value.getToken());
            }
            else {
                bundle1.putSerializable("status", FetchStatus.ERROR);
                bundle1.putSerializable("user", null);
                bundle1.putString("user", null);
            }
            fetchStatusMutableLiveData.setValue(bundle1);
        }).execute(bundle);
        return fetchStatusMutableLiveData;
    }
}
