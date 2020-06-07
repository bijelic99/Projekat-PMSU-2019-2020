package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.FolderSyncWrapper;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FolderAsyncTasks {
    public static class FolderFetchAsyncTask extends AsyncTask<Long, Void, Folder> {
        private LocalDatabase localDatabase;

        public FolderFetchAsyncTask(LocalDatabase localDatabase){
            this.localDatabase = localDatabase;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Folder doInBackground(Long... longs) {
            FolderDao folderDao = localDatabase.folderDao();
            Folder folder = folderDao.getFolderById(longs[0]);
            if(folder == null) {
                FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
                try {
                    Response<Folder> response = folderApi.getFolder(longs[0]).execute();
                    if (response.isSuccessful()) {
                        folder = response.body();
                        localDatabase.messageDao().insertAll(folder.getMessages().stream().collect(Collectors.toList()));
                        folderDao.insert(folder);
                        return folder;
                    } else return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
            else return null;
        }
    }

    public static class FolderSyncAsyncTask extends AsyncTask<Long, Void, Folder>{
        private LocalDatabase localDatabase;

        public FolderSyncAsyncTask(LocalDatabase localDatabase){
            this.localDatabase = localDatabase;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Folder doInBackground(Long... longs) {
            FolderDao folderDao = localDatabase.folderDao();
            MessageDao messageDao = localDatabase.messageDao();

            Folder folder = folderDao.getFolderById(longs[0]);
            List<Message> messages = messageDao.getCurrentMessages(folder.getMessages().stream().map(message -> message.getId()).collect(Collectors.toList()));

            Map<String, Object> syncData = new HashMap<>();
            syncData.put("latestMessageTimestamp", folder.getMessages().stream()
                    .map(message -> message.getDateTime())
                    .max((o1, o2) -> o1.isAfter(o2) ? 1 : o1.isBefore(o2) ? -1 : 0)
                    .orElse(null));
            syncData.put("folder_list", folder.getFolders().stream()
                    .map(folderMetadata -> folderMetadata.getId())
                    .collect(Collectors.toSet()));

            FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
            Response<FolderSyncWrapper> response = null;
            try {
                response = folderApi.syncFolder(longs[0], syncData).execute();
                if(response.isSuccessful()){
                    List<FolderMetadata> folderMetadataList = response.body().getFolders().stream()
                            .map(folder1 -> new FolderMetadata(folder1))
                            .collect(Collectors.toList());
                    folder.getFolders().addAll(folderMetadataList);
                    folderDao.update(folder);
                    return folder;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


            return null;
        }
    }
}
