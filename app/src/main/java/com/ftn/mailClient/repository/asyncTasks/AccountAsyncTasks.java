package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.retrofit.AccountApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import com.ftn.mailClient.utill.enums.FetchStatus;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class AccountAsyncTasks {

    public static class FetchAccountAsyncTask extends AsyncTask<Long, Void, Account>{
        private LocalDatabase localDatabase;

        public FetchAccountAsyncTask(LocalDatabase localDatabase) {
            this.localDatabase = localDatabase;
        }

        @Override
        protected Account doInBackground(Long... longs) {
            AccountDao accountDao = localDatabase.accountDao();
            Account account = accountDao.getAccountById(longs[0]);
            if(account == null){
                AccountApi api = RetrofitClient.getApi(AccountApi.class);
                Response<Account> accountResponse = null;
                try {
                    accountResponse = api.getAccount(longs[0]).execute();
                    if(accountResponse.isSuccessful()){
                        account = accountResponse.body();
                        accountDao.insert(account);
                        return account;
                    }
                    else return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
            else return account;
        }
    }

    public static class FetchAccountFoldersAsyncTask extends AsyncTask<Long, Void, Boolean>{
        private LocalDatabase localDatabase;
        OnPostExecuteFunctionFunctionalInterface<Boolean> postExecuteFunctionalInterface;

        public FetchAccountFoldersAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> postExecuteFunctionalInterface){
            this.localDatabase = localDatabase;
            this.postExecuteFunctionalInterface = postExecuteFunctionalInterface;
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
            Long accountId = longs[0];
            AccountDao accountDao = localDatabase.accountDao();
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);
            Account account = accountDao.getAccountById(accountId);
            try {
                Response<List<FolderMetadata>> foldersResponse = accountApi.getAccountFolders(accountId).execute();
                if(foldersResponse.isSuccessful()){
                    List<FolderMetadata> folders = foldersResponse.body();
                    account.setAccountFolders(folders);
                    accountDao.update(account);
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
