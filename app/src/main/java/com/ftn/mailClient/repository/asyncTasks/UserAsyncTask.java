package com.ftn.mailClient.repository.asyncTasks;

import com.ftn.mailClient.dao.RuleDao;
import com.ftn.mailClient.dao.UserDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Rule;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.RuleApi;
import com.ftn.mailClient.retrofit.UserApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;

import java.io.IOException;

import retrofit2.Response;

public class UserAsyncTask {
    public static class AddNewUserAsyncTask extends LocalDatabaseCallbackAsyncTask<User, Void, Boolean>{
        private Long accountId;

        public AddNewUserAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long accountId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.accountId = accountId;
        }

        @Override
        protected Boolean doInBackground(User... users) {
            User user = users[0];
            UserDao userDao = localDatabase.userDao();
            UserApi userApi = RetrofitClient.getApi(UserApi.class);

            try {
                Response<User> response = userApi.addUser(accountId, user).execute();
                if(response.isSuccessful()){
                    user = response.body();
                    userDao.insert(user);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
