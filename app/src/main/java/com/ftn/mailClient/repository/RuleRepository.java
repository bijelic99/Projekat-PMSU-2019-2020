package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.RuleDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Rule;
import com.ftn.mailClient.repository.asyncTasks.RuleAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class RuleRepository extends Repository<Rule, RuleDao> {
    public RuleRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.ruleDao();
    }

    @Override
    public LiveData<FetchStatus> insert(Rule value) {
        return null;
    }

    public LiveData<FetchStatus> insert(Long accountId, Rule rule){
        MutableLiveData<FetchStatus> mutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new RuleAsyncTasks.AddNewRuleAsyncTask(database, value -> mutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR), accountId).execute(rule);
        return mutableLiveData;
    }

    @Override
    public void update(Rule value) {

    }

    @Override
    public void delete(Rule value) {

    }

    @Override
    public LiveData<List<Rule>> getAll() {
        return null;
    }

    @Override
    public LiveData<Rule> getById(Long id) {
        return null;
    }

    @Override
    public LiveData<List<Rule>> getByIdList(List<Long> ids) {
        return null;
    }
}
