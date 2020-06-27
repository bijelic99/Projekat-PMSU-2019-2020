package com.ftn.mailClient.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.repository.FolderRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

public class CreateFolderViewModel extends AndroidViewModel {
    Folder newFolder;
    FolderRepository folderRepository;
    Long accountId;
    Boolean accountFolder;
    Long parentFolderId;
    LiveData<FolderMetadata> parentFolder;

    public CreateFolderViewModel(@NonNull Application application) {
        super(application);
        newFolder = new Folder();
        newFolder.setParentFolder(null);
        folderRepository = new FolderRepository(application);
        accountFolder = true;
    }

    public void setParentFolder(Long parentFolderId){
        this.parentFolderId = parentFolderId;
        accountFolder = false;

    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public Boolean isAccountFolder() {
        return accountFolder;
    }

    public void setFolderName(String name){
        if(name == null || name.equals("")) throw new NullPointerException("name cannot be null");
        newFolder.setName(name);
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LiveData<FetchStatus> addNewFolder(){
        if(newFolder.getName() == null || newFolder.getName().equals("")) throw new NullPointerException("name cannot be null");
        return accountId != null ? folderRepository.insertAccountFolder(newFolder, accountId) : folderRepository.insertFolderWithParent(newFolder, parentFolderId);

    }

    public LiveData<FolderMetadata> getParentFolder(){
        if (parentFolder == null) parentFolder = folderRepository.getMetadataFolderById(parentFolderId);
        return parentFolder;
    }


}
