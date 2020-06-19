package com.ftn.mailClient.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ftn.mailClient.dao.TagDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.asyncTasks.ContactAsyncTasks;
import com.ftn.mailClient.repository.asyncTasks.TagAsyncTask;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class TagRepository extends Repository<Tag, TagDao> {

    public TagRepository(Application application){
        super(application);
        LocalDatabase localDatabase = LocalDatabase.getInstance(application);
        dao = localDatabase.tagDao();
    }

    @Override
    public LiveData<FetchStatus> insert(Tag value, Long userID) {
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new TagAsyncTask.InsertTagAsyncTask(database, value1 -> fetchStatusMutableLiveData.setValue(value1 ? FetchStatus.DONE : FetchStatus.ERROR), userID).execute(value);
        return fetchStatusMutableLiveData;
    }

    @Override
    public void update(Tag value) {

    }

    @Override
    public void delete(Tag value) {

    }

    @Override
    public LiveData<List<Tag>> getAll() {
        return dao.getAllTags();
    }

    @Override
    public LiveData<Tag> getById(Long id) {
        LiveData<Tag> tagLiveData = dao.getLiveDataTagById(id);
        return tagLiveData;
    }

    @Override
    public LiveData<List<Tag>> getByIdList(List<Long> ids) {
        return dao.getTags(ids);
    }

    public LiveData<FetchStatus> fetchTags(Long accountId){
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new TagAsyncTask.FetchTagsAsyncTask(database, value -> fetchStatusMutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR)).execute(accountId);
        return fetchStatusMutableLiveData;
    }
}
