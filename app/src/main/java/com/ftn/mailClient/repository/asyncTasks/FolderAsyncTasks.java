package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.linkingClasses.FolderMessage;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.FolderSyncWrapper;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
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
                        localDatabase.messageDao().insertAll(folder.getMessages());
                        folderDao.insert(folder);
                        Folder finalFolder = folder;
                        folderDao.insertMessagesToFolder(folder.getMessages().stream()
                                .map(message -> new FolderMessage(finalFolder.getId(), message.getId()))
                                .collect(Collectors.toList()));
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

    public static class FolderSyncAsyncTask extends AsyncTask<Long, Void, Boolean>{
        private LocalDatabase localDatabase;
        private OnPostExecuteFunctionFunctionalInterface onPostExecuteFunctionFunctionalInterface;

        public FolderSyncAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface){
            this.localDatabase = localDatabase;
            this.onPostExecuteFunctionFunctionalInterface = onPostExecuteFunctionFunctionalInterface;
        }

        public FolderSyncAsyncTask(LocalDatabase localDatabase){
            this.localDatabase = localDatabase;
            this.onPostExecuteFunctionFunctionalInterface = null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Long... longs) {
            FolderDao folderDao = localDatabase.folderDao();
            MessageDao messageDao = localDatabase.messageDao();

            Folder folder = folderDao.getFolderById(longs[0]);
            List<Message> messages = folderDao.getMessagesNonLive(folder.getId());
            List<FolderMetadata> folders = folderDao.getFoldersNonLive(folder.getId());

            Map<String, Object> syncData = new HashMap<>();
            syncData.put("latestMessageTimestamp", messages.stream()
                    .map(message -> message.getDateTime())
                    .max((o1, o2) -> o1.isAfter(o2) ? 1 : o1.isBefore(o2) ? -1 : 0)
                    .orElse(null));
            if(folders != null) {
                syncData.put("folder_list", folders.stream()
                        .map(folderMetadata -> folderMetadata.getId())
                        .collect(Collectors.toList()));
            }
            else syncData.put("folder_list", new ArrayList<Long>());

            FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
            Response<FolderSyncWrapper> response = null;
            try {
                response = folderApi.syncFolder(longs[0], syncData).execute();
                if(response.isSuccessful()){
                    List<FolderMetadata> folderMetadataList = response.body().getFolders().stream()
                            .map(folder1 -> new FolderMetadata(folder1))
                            .collect(Collectors.toList());
                    List<Message> messages1 = response.body().getMessages();
                    messageDao.insertAll(messages1);

                    folder.getFolders().addAll(folderMetadataList);
                    folderDao.update(folder);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Its Me Mario", e.getMessage());
                return false;
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(onPostExecuteFunctionFunctionalInterface != null) onPostExecuteFunctionFunctionalInterface.postExecuteFunction(aBoolean);
        }
    }
}
