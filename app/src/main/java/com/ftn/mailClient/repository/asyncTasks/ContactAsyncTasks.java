package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ftn.mailClient.dao.ContactDao;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.linkingClasses.FolderInnerFolders;
import com.ftn.mailClient.model.linkingClasses.FolderMessage;
import com.ftn.mailClient.retrofit.ContactApi;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.UserApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ContactAsyncTasks {
    public static class FetchContactsAsyncTask extends LocalDatabaseCallbackAsyncTask<Long, Void, Boolean> {
        public FetchContactsAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
            ContactDao contactDao = localDatabase.contactDao();
            UserApi userApi = RetrofitClient.getApi(UserApi.class);
            Long userId = longs[0];

            try {
                Response<List<Contact>> response = userApi.getUserContacts(userId).execute();
                if(response.isSuccessful()){
                    List<Contact> contacts = response.body();
                    contactDao.insertAll(contacts);
                    return true;
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return false;
        }
    }

    public static class ContactFetchAsyncTask extends AsyncTask<Long, Void, Contact> {
        private LocalDatabase localDatabase;

        public ContactFetchAsyncTask(LocalDatabase localDatabase){
            this.localDatabase = localDatabase;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Contact doInBackground(Long... longs) {
            ContactDao contactDao = localDatabase.contactDao();
            Contact contact = contactDao.getContactById(longs[0]);
            if(contact == null) {
                ContactApi contactApi = RetrofitClient.getApi(ContactApi.class);
                try {
                    Response<Contact> response = contactApi.getContact(longs[0]).execute();
                    if (response.isSuccessful()) {
                        contact = response.body();
                        contactDao.insert(contact);

                        return contact;
                    } else return null;
                } catch (IOException e) {
                    e.printStackTrace();

                    return null;
                }

            }
            else return null;
        }
    }

    public static class ContactSyncAsyncTask extends AsyncTask<Long, Void, Boolean>{
        private LocalDatabase localDatabase;
        private OnPostExecuteFunctionFunctionalInterface onPostExecuteFunctionFunctionalInterface;

        public ContactSyncAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface){
            this.localDatabase = localDatabase;
            this.onPostExecuteFunctionFunctionalInterface = onPostExecuteFunctionFunctionalInterface;
        }

        public ContactSyncAsyncTask(LocalDatabase localDatabase){
            this.localDatabase = localDatabase;
            this.onPostExecuteFunctionFunctionalInterface = null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Long... longs) {
            ContactDao contactDao = localDatabase.contactDao();
            //MessageDao messageDao = localDatabase.messageDao();

            Long contactId = longs[0];

            Contact contact = contactDao.getContactById(contactId);


            ContactApi contactApi = RetrofitClient.getApi(ContactApi.class);
            Response<Contact> response = null;
            try {
                response = contactApi.syncContact(contactId).execute();
                if(response.isSuccessful()){
                    contact = response.body();

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
    public static class InsertContactAsyncTask extends LocalDatabaseCallbackAsyncTask<Contact, Void, Boolean>{
        private Long userId;

        public InsertContactAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long userId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(Contact... contacts) {
            ContactDao contactDao = localDatabase.contactDao();
            UserApi userApi = RetrofitClient.getApi(UserApi.class);
            Contact contact = contacts[0];

            try {
                Response<Contact> response = userApi.addUserContact(userId, contact).execute();
                if(response.isSuccessful()){
                    contact = response.body();
                    contactDao.insert(contact);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
