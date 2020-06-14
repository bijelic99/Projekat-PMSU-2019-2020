package com.ftn.mailClient.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ftn.mailClient.dao.TagDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class TagRepository extends Repository<Tag, TagDao> {

    public TagRepository(Application application){
        super(application);
        LocalDatabase localDatabase = LocalDatabase.getInstance(application);
        dao = localDatabase.tagDao();
    }

    @Override
    public LiveData<FetchStatus> insert(Tag value) {
       return null;
    }

    @Override
    public void update(Tag value) {

    }

    @Override
    public void delete(Tag value) {

    }

    @Override
    public LiveData<List<Tag>> getAll() {
        LiveData<List<Tag>> tags = dao.getTags();
        return tags;
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
}
