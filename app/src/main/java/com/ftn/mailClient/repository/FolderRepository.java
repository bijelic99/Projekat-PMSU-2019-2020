package com.ftn.mailClient.repository;

import android.app.Application;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.asyncTasks.FolderAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

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
    public LiveData<FetchStatus> insert(Tag value, Long userID) {
        return null;
    }

    public LiveData<FetchStatus> insertFolderWithParent(Folder value, Long parentFolderId) {
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new FolderAsyncTasks.InsertFolderAsyncTask(database, parentFolderId, aboolean -> {
            if(aboolean) fetchStatusMutableLiveData.setValue(FetchStatus.DONE);
            else fetchStatusMutableLiveData.setValue(FetchStatus.ERROR);
        }).execute(value);
        return fetchStatusMutableLiveData;
    }

    public LiveData<FetchStatus> insertAccountFolder(Folder value, Long accountId){
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new FolderAsyncTasks.InsertAccountFolderAsyncTask(database, accountId, aboolean -> {
            if(aboolean) fetchStatusMutableLiveData.setValue(FetchStatus.DONE);
            else fetchStatusMutableLiveData.setValue(FetchStatus.ERROR);
        }).execute(value);
        return fetchStatusMutableLiveData;
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

    public LiveData<FolderMetadata> getMetadataFolderById(Long id){
        return dao.getFolderMetadataById(id);
    }

    @Override
    public LiveData<List<Folder>> getByIdList(List<Long> ids) {
        return null;
    }

    public LiveData<List<FolderMetadata>> getFolders(Long folderId){
        return dao.getFolders(folderId);
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
