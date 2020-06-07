package com.ftn.mailClient.repository;

import android.app.Application;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.repository.asyncTasks.FolderAsyncTasks;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FolderRepository extends Repository<Folder, FolderDao> {

    private LiveData<List<Folder>> allFolders;

    public FolderRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.folderDao();
        allFolders = dao.getAllFolders();
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
        MutableLiveData<Folder> folderMutableLiveData = new MutableLiveData<>(null);

        folderLiveData.observe((LifecycleOwner) application.getBaseContext(), folder -> {
            Set<Message> messages = new HashSet<>(Objects.requireNonNull(database.messageDao().getMessages(
                    folder.getMessages().stream()
                            .map(value -> value.getId())
                            .collect(Collectors.toList())
            ).getValue()));
            folder.setMessages(messages);
            folderMutableLiveData.setValue(folder);
        });


        return (LiveData<Folder>)folderMutableLiveData;
    }

    @Override
    public LiveData<List<Folder>> getByIdList(List<Long> ids) {
        return null;
    }

    public void fetchFolder(Long folderId){
        new FolderAsyncTasks.FolderFetchAsyncTask(database).execute(folderId);
    }

    public void syncFolder(Long folderId){
        new FolderAsyncTasks.FolderSyncAsyncTask(database).execute(folderId);
    }
}
