package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.R;
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
    private Long accountId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FolderViewModel(@NonNull Application application) {
        super(application);
        folderRepository = new FolderRepository(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(application.getString(R.string.user_account_id))) {
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        } else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
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

    public LiveData<FolderMetadata> getAnyFolder(Long folderId){
        return null;
    }

    public void executeRuleSet(){
        Toast.makeText(getApplication(), R.string.executing_rules, Toast.LENGTH_SHORT).show();
        folderRepository.executeRulesetOnFolder(folderId, accountId);
    }
}
