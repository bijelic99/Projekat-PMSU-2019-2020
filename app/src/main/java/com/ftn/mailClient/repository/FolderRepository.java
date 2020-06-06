package com.ftn.mailClient.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;

import java.util.List;
import java.util.Set;

public class FolderRepository extends Repository<Folder, FolderDao> {

    public FolderRepository(Application application) {
        super(application);
        LocalDatabase database = LocalDatabase.getInstance(application);
        dao = database.folderDao();
    }

    @Override
    public void insert(Folder value) {

    }

    @Override
    public void update(Folder value) {

    }

    @Override
    public void delete(Folder value) {

    }

    @Override
    public LiveData<Set<Folder>> getAll() {
        AsyncTask<Void, Void, List<Folder>> asyncTask = new AsyncTask<Void, Void, List<Folder>>() {
            @Override
            protected List<Folder> doInBackground(Void... voids) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Folder getById(Long id) {
        return null;
    }


}
