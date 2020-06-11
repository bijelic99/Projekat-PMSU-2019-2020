package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.linkingClasses.AccountFolder;
import com.ftn.mailClient.retrofit.AccountApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import com.ftn.mailClient.utill.enums.FetchStatus;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AccountAsyncTasks {

    public static class FetchAccountAsyncTask extends AsyncTask<Long, Void, Account>{
        private LocalDatabase localDatabase;

        public FetchAccountAsyncTask(LocalDatabase localDatabase) {
            this.localDatabase = localDatabase;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Account doInBackground(Long... longs) {
            AccountDao accountDao = localDatabase.accountDao();
            FolderDao folderDao = localDatabase.folderDao();
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);
            try {
                Response<Account>  accountResponse = accountApi.getAccount(longs[0]).execute();
                if(accountResponse.isSuccessful()){
                    Account account = accountResponse.body();
                    folderDao.insertAll(account.getAccountFolders().stream()
                            .map(folderMetadata -> new Folder(folderMetadata.getId(), folderMetadata.getName(), null))
                            .collect(Collectors.toList())
                    );
                    List<AccountFolder> accountFolders = account.getAccountFolders().stream()
                            .map(folderMetadata -> new AccountFolder(account.getId(), folderMetadata.getId()))
                            .collect(Collectors.toList());
                    accountDao.insert(account);
                    accountDao.insertAccountFolders(accountFolders);



                    return account;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class FetchAccountFoldersAsyncTask extends AsyncTask<Long, Void, Boolean>{
        private LocalDatabase localDatabase;
        OnPostExecuteFunctionFunctionalInterface<Boolean> postExecuteFunctionalInterface;

        public FetchAccountFoldersAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> postExecuteFunctionalInterface){
            this.localDatabase = localDatabase;
            this.postExecuteFunctionalInterface = postExecuteFunctionalInterface;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Boolean doInBackground(Long... longs) {
            Long accountId = longs[0];
            AccountDao accountDao = localDatabase.accountDao();
            FolderDao folderDao = localDatabase.folderDao();
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);
            Account account = accountDao.getAccountById(accountId);
            try {
                Response<List<FolderMetadata>> foldersResponse = accountApi.getAccountFolders(accountId).execute();
                if(foldersResponse.isSuccessful()){
                    List<FolderMetadata> folders = foldersResponse.body();
                    folderDao.insertAll(folders.stream()
                            .map(folderMetadata -> new Folder(folderMetadata.getId(), folderMetadata.getName(), null))
                            .collect(Collectors.toList()));

                    accountDao.insertAccountFolders(folders.stream()
                            .map(folderMetadata -> new AccountFolder(account.getId(), folderMetadata.getId()))
                            .collect(Collectors.toList()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            postExecuteFunctionalInterface.postExecuteFunction(aBoolean);
        }

    }


}
