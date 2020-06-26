package com.ftn.mailClient.repository.asyncTasks;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.dao.UserDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.retrofit.AccountApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.UserApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import com.ftn.mailClient.utill.UserTokenWrapper;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserAsyncTasks {
    public static class LoginUserAsyncTask extends LocalDatabaseCallbackAsyncTask<Bundle, Void, UserTokenWrapper> {
        public LoginUserAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<UserTokenWrapper> onPostExecuteFunctionFunctionalInterface) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
        }

        @Override
        protected UserTokenWrapper doInBackground(Bundle... bundles) {
            UserApi userApi = RetrofitClient.getApi(UserApi.class);
            UserDao userDao = localDatabase.userDao();
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);
            AccountDao accountDao = localDatabase.accountDao();

            String username = bundles[0].getString("username");
            String password = bundles[0].getString("password");

            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password", password);

            try {
                Response<UserTokenWrapper> response = userApi.login(map).execute();
                if(response.isSuccessful()){
                    UserTokenWrapper userTokenWrapper = response.body();
                    userDao.insert(userTokenWrapper.getUser());
                    new AccountAsyncTasks.FetchUserAccountsAsyncTask(localDatabase, value -> {}, userTokenWrapper.getUser().getId()).execute();
                    return userTokenWrapper;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
