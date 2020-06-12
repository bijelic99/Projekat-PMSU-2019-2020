package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.linkingClasses.AccountFolder;
import com.ftn.mailClient.model.linkingClasses.FolderInnerFolders;
import com.ftn.mailClient.model.linkingClasses.FolderMessage;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.FolderSyncWrapper;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

            Long folderId = longs[0];

            Folder folder = folderDao.getFolderById(folderId);


            FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
            Response<Folder> response = null;
            try {
                response = folderApi.syncFolder(folderId).execute();
                if(response.isSuccessful()){
                    folder = response.body();
                    Folder finalFolder = folder;
                    messageDao.insertAll(folder.getMessages());
                    folderDao.deleteAllFolderMessages(folder.getId());
                    folderDao.insertMessagesToFolder(folder.getMessages().stream()
                            .map(message -> new FolderMessage(finalFolder.getId(), message.getId()))
                            .collect(Collectors.toList()));
                    folderDao.insertAll(folder.getFolders().stream()
                            .map(folderMetadata -> new Folder(folderMetadata.getId(), folderMetadata.getName(), new FolderMetadata(finalFolder)))
                            .collect(Collectors.toList()));
                    folderDao.deleteAllFolderChlildrenFolders(folder.getId());
                    folderDao.insertFoldersToFolder(folder.getFolders().stream()
                            .map(folderMetadata -> new FolderInnerFolders(finalFolder.getId(), folderMetadata.getId()))
                            .collect(Collectors.toList()));


                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(onPostExecuteFunctionFunctionalInterface != null) onPostExecuteFunctionFunctionalInterface.postExecuteFunction(aBoolean);
        }
    }

    public static class InsertAccountFolderAsyncTask extends AsyncTask<Folder, Void, Boolean>{
        private LocalDatabase localDatabase;
        private OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface;
        private Long accountId;

        public InsertAccountFolderAsyncTask(LocalDatabase localDatabase, Long accountId, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface){
            this.localDatabase = localDatabase;
            this.accountId = accountId;
            this.onPostExecuteFunctionFunctionalInterface = onPostExecuteFunctionFunctionalInterface;
        }

        @Override
        protected Boolean doInBackground(Folder... folders) {
            FolderDao folderDao = localDatabase.folderDao();
            FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
            Folder folder = folders[0];

            if( accountId != null){
                try {
                    folder.setParentFolder(null);
                    Response<Folder> folderResponse = folderApi.addAccountFolder(accountId, folder).execute();
                    if(folderResponse.isSuccessful()){
                        AccountDao accountDao = localDatabase.accountDao();
                        folder = folderResponse.body();
                        folderDao.insert(folder);
                        accountDao.insertAccountFolders(Arrays.asList(new AccountFolder(accountId, folder.getId())));

                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            onPostExecuteFunctionFunctionalInterface.postExecuteFunction(aBoolean);
        }
    }

    public static class InsertFolderAsyncTask extends AsyncTask<Folder, Void, Boolean>{
        private LocalDatabase localDatabase;
        private OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface;
        private Long parentFolderId;

        public InsertFolderAsyncTask(LocalDatabase localDatabase, Long parentFolderId, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface){
            this.localDatabase = localDatabase;
            this.parentFolderId = parentFolderId;
            this.onPostExecuteFunctionFunctionalInterface = onPostExecuteFunctionFunctionalInterface;
        }

        @Override
        protected Boolean doInBackground(Folder... folders) {
            FolderDao folderDao = localDatabase.folderDao();
            FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
            Folder folder = folders[0];
            FolderMetadata parentFolder = folderDao.getFolderMetadataByIdNonLive(parentFolderId);


            if(parentFolderId != null){
                try {
                    folder.setParentFolder(parentFolder);
                    Response<Folder> folderResponse = folderApi.saveFolder(folder).execute();
                    if(folderResponse.isSuccessful()){
                        folder = folderResponse.body();
                        folderDao.insert(folder);
                        folderDao.insertFoldersToFolder(Arrays.asList(new FolderInnerFolders(parentFolderId, folder.getId())));
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            onPostExecuteFunctionFunctionalInterface.postExecuteFunction(aBoolean);
        }
    }
}
