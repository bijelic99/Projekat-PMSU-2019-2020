package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.dao.DaoInterface;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Identifiable;
import com.ftn.mailClient.model.Message;

import java.util.List;

public abstract class Repository<T extends Identifiable, D extends DaoInterface> {

    protected D dao;
    protected LocalDatabase database;
    protected Application application;

    public Repository(Application application){
        this.application = application;
    }

    public abstract void insert(T value);
    public abstract void update(T value);
    public abstract void delete(T value);
    public abstract LiveData<List<T>> getAll();
    public abstract LiveData<T> getById(Long id);
    public abstract LiveData<List<T>> getByIdList(List<Long> ids);
}
