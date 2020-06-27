package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.*;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
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

    public static class FetchAccountAsyncTask extends AsyncTask<Long, Void, Account> {
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
                Response<Account> accountResponse = accountApi.getAccount(longs[0]).execute();
                if (accountResponse.isSuccessful()) {
                    Account account = accountResponse.body();
                    folderDao.insertAll(account.getAccountFolders().stream()
                            .map(folderMetadata -> new Folder(folderMetadata.getId(), folderMetadata.getName(), null))
                            .collect(Collectors.toList())
                    );
                    accountDao.deleteAllAccountFolders(account.getId());
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

    public static class FetchAccountFoldersAsyncTask extends AsyncTask<Long, Void, Boolean> {
        private LocalDatabase localDatabase;
        OnPostExecuteFunctionFunctionalInterface<Boolean> postExecuteFunctionalInterface;

        public FetchAccountFoldersAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> postExecuteFunctionalInterface) {
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
                if (foldersResponse.isSuccessful()) {
                    List<FolderMetadata> folders = foldersResponse.body();
                    folderDao.insertAll(folders.stream()
                            .map(folderMetadata -> new Folder(folderMetadata.getId(), folderMetadata.getName(), null))
                            .collect(Collectors.toList()));
                    accountDao.deleteAllAccountFolders(accountId);
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

    public static class FetchAccountMessagesAsyncTask extends AsyncTask<Long, Void, Boolean> {
        private LocalDatabase localDatabase;
        private OnPostExecuteFunctionFunctionalInterface onPostExecuteFunctionFunctionalInterface;

        public FetchAccountMessagesAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface) {
            this.localDatabase = localDatabase;
            this.onPostExecuteFunctionFunctionalInterface = onPostExecuteFunctionFunctionalInterface;
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
            Long accountId = longs[0];
            MessageDao messageDao = localDatabase.messageDao();
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);

            try {
                Response<List<Message>> response = accountApi.getAccountMessages(accountId).execute();
                if (response.isSuccessful()) {
                    List<Message> messages = response.body();
                    messageDao.insertAll(messages);
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
            onPostExecuteFunctionFunctionalInterface.postExecuteFunction(aBoolean);
        }
    }

    public static class FetchAccountTagsAsyncTask extends AsyncTask<Long, Void, Boolean> {
        private LocalDatabase localDatabase;
        private OnPostExecuteFunctionFunctionalInterface onPostExecuteFunctionFunctionalInterface;

        public FetchAccountTagsAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface) {
            this.localDatabase = localDatabase;
            this.onPostExecuteFunctionFunctionalInterface = onPostExecuteFunctionFunctionalInterface;
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
            Long accountId = longs[0];
            TagDao tagDao = localDatabase.tagDao();
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);

            try {
                Response<List<Tag>> response = accountApi.getAccountTags(accountId).execute();
                if (response.isSuccessful()) {
                    List<Tag> tags = response.body();
                    tagDao.insertAll(tags);
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
            onPostExecuteFunctionFunctionalInterface.postExecuteFunction(aBoolean);
        }
    }

    public static class FetchUserAccountsAsyncTask extends LocalDatabaseCallbackAsyncTask<Long, Void, Boolean> {
        private Long userId;

        public FetchUserAccountsAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long userId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);
            AccountDao accountDao = localDatabase.accountDao();

            try {
                Response<List<Account>> response = accountApi.getUserAccounts(userId).execute();
                if (response.isSuccessful()) {
                    accountDao.insertAll(response.body());
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public static class AddNewUserAccountAsyncTask extends LocalDatabaseCallbackAsyncTask<Account, Void, Boolean> {
        private Long userId;

        public AddNewUserAccountAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long userId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(Account... accounts) {
            Account account = accounts[0];
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);
            AccountDao accountDao = localDatabase.accountDao();

            try {
                Response<Account> response = accountApi.addUserAccount(userId, account).execute();
                if (response.isSuccessful()) {
                    account = response.body();
                    accountDao.insert(account);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public static class ChangeAccountAsyncTask extends LocalDatabaseCallbackAsyncTask<Void, Void, Boolean> {
        private Long userId;
        private Long accountId;

        public ChangeAccountAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long userId, Long accountId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.userId = userId;
            this.accountId = accountId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            AccountApi accountApi = RetrofitClient.getApi(AccountApi.class);
            MessageDao messageDao = localDatabase.messageDao();
            FolderDao folderDao = localDatabase.folderDao();
            RuleDao ruleDao = localDatabase.ruleDao();
            AccountDao accountDao = localDatabase.accountDao();


            try {
                Response<Account> response = accountApi.getAccount(accountId).execute();
                if (response.isSuccessful()) {
                    Account account = response.body();

                    folderDao.deleteTableAccountFolder();
                    folderDao.deleteTableFolderInnerFolders();
                    folderDao.deleteTableFolderMessage();
                    messageDao.deleteTable();
                    folderDao.deleteTable();
                    ruleDao.deleteTable();

                    accountDao.insert(account);


                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
