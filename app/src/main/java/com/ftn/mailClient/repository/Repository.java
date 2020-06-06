package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.dao.DaoInterface;
import com.ftn.mailClient.model.Identifiable;

import java.util.List;
import java.util.Set;

public abstract class Repository<T extends Identifiable, D extends DaoInterface> {

    protected D dao;

    public Repository(Application application){

    }

    public abstract void insert(T value);
    public abstract void update(T value);
    public abstract void delete(T value);
    public abstract LiveData<Set<T>> getAll();
    public abstract T getById(Long id);
}
