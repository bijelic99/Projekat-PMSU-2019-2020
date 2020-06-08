package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.repository.FolderRepository;

public class FolderViewModel extends AndroidViewModel {
    private FolderRepository folderRepository;
    private LiveData<Folder> folder;
    private Long folderId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FolderViewModel(@NonNull Application application) {
        super(application);
        folderRepository = new FolderRepository(application);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setFolder(Long id){
        folder = folderRepository.getById(id);
    }

    public LiveData<Folder> getFolder(){
        return folder;
    }
}
