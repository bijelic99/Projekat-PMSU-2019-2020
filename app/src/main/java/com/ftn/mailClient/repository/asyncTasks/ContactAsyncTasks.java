package com.ftn.mailClient.repository.asyncTasks;

import com.ftn.mailClient.dao.ContactDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.UserApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

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
