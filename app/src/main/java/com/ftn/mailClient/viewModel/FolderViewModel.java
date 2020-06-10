package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.repository.FolderRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class FolderViewModel extends AndroidViewModel {
    private FolderRepository folderRepository;
    private LiveData<FolderMetadata> folder;
    private Long folderId;
    private LiveData<List<Message>> messages;
    private LiveData<List<FolderMetadata>> folders;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FolderViewModel(@NonNull Application application) {
        super(application);
        folderRepository = new FolderRepository(application);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setFolder(Long id){
        folderId = id;
        folderRepository.fetchFolder(id);
        folder = folderRepository.getMetadataFolderById(id);
    }

    public LiveData<FolderMetadata> getFolder(){
        return folder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Message>> getMessages() {
        if(messages == null ) {
            messages = folderRepository.getMessages(folderId);
        }
        return messages;
    }

    public LiveData<List<FolderMetadata>> getFolders(){
        if(folders == null) folders = folderRepository.getFolders(folderId);
        return folders;
    }

    public LiveData<FetchStatus> syncFolder(){
        return folderRepository.syncFolder(folderId);
    }

    public Long getFolderId() {
        return folderId;
    }
}
