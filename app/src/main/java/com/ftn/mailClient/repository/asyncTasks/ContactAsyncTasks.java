package com.ftn.mailClient.repository.asyncTasks;

import com.ftn.mailClient.dao.ContactDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.UserApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import retrofit2.Response;

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
}
