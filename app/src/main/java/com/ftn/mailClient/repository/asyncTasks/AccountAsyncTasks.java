package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.retrofit.AccountApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import retrofit2.Response;

import java.io.IOException;

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
}
