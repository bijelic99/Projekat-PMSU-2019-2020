package com.ftn.mailClient.repository;

import android.app.Application;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.repository.asyncTasks.FolderAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FolderRepository extends Repository<Folder, FolderDao> {

    private LiveData<List<Folder>> allFolders;
    private MessageDao messageDao;


    public FolderRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.folderDao();
        allFolders = dao.getAllFolders();
        messageDao = database.messageDao();
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
    public LiveData<List<Folder>> getAll() {
        
        return allFolders;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public LiveData<Folder> getById(Long id) {
        LiveData<Folder> folderLiveData = dao.getLiveDataFolderById(id);
        if(folderLiveData.getValue() == null) fetchFolder(id);


        return folderLiveData;
    }

    @Override
    public LiveData<List<Folder>> getByIdList(List<Long> ids) {
        return null;
    }

    public void fetchFolder(Long folderId){
        new FolderAsyncTasks.FolderFetchAsyncTask(database).execute(folderId);
    }

    public LiveData<FetchStatus> syncFolder(Long folderId){
        MutableLiveData<FetchStatus> fetchStatus = new MutableLiveData<>(FetchStatus.FETCHING);
        new FolderAsyncTasks.FolderSyncAsyncTask(database, value -> {
            if(value) fetchStatus.setValue(FetchStatus.DONE);
            else fetchStatus.setValue(FetchStatus.ERROR);
        }).execute(folderId);
        return fetchStatus;
    }

    public LiveData<List<Message>> getMessages(Long folderId){
        return dao.getMessages(folderId);
    }
}
