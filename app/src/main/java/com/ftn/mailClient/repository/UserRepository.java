package com.ftn.mailClient.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ftn.mailClient.dao.UserDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Rule;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.asyncTasks.RuleAsyncTasks;
import com.ftn.mailClient.repository.asyncTasks.UserAsyncTask;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class UserRepository extends Repository<User, UserDao> {
    public UserRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.userDao();
    }

    @Override
    public LiveData<FetchStatus> insert(User value) {
        return null;
    }

    public LiveData<FetchStatus> insert(Long accountId, User user){
        MutableLiveData<FetchStatus> mutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new UserAsyncTask.AddNewUserAsyncTask(database, value -> mutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR), accountId).execute(user);
        return mutableLiveData;
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
}
