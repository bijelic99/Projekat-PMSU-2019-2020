package com.ftn.mailClient.repository.asyncTasks;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.ftn.mailClient.dao.*;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.retrofit.AccountApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.UserApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import com.ftn.mailClient.utill.UserTokenWrapper;
import com.ftn.mailClient.utill.enums.FetchStatus;
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


    public static class LogoutUserAsyncTask extends LocalDatabaseCallbackAsyncTask<Void, Void, Boolean>{

        public LogoutUserAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            AccountDao accountDao = localDatabase.accountDao();
            ContactDao contactDao = localDatabase.contactDao();
            FolderDao folderDao = localDatabase.folderDao();
            MessageDao messageDao = localDatabase.messageDao();
            RuleDao ruleDao = localDatabase.ruleDao();
            TagDao tagDao = localDatabase.tagDao();
            UserDao userDao =  localDatabase.userDao();

            folderDao.deleteTableAccountFolder();;
            folderDao.deleteTableFolderInnerFolders();
            folderDao.deleteTableFolderMessage();
            messageDao.deleteTable();
            ruleDao.deleteTable();
            tagDao.deleteTable();
            contactDao.deleteTable();
            folderDao.deleteTable();
            accountDao.deleteTable();
            userDao.deleteTable();

            return true;
        }
    }

    public static class RegisterNewUserAsyncTask extends LocalDatabaseCallbackAsyncTask<User, Void, Boolean>{

        public RegisterNewUserAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
        }

        @Override
        protected Boolean doInBackground(User... users) {
            User user = users[0];
            UserApi userApi = RetrofitClient.getApi(UserApi.class);

            user.setId(null);

            try {
                Response<User> response = userApi.register(user).execute();
                if(response.isSuccessful()){
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
