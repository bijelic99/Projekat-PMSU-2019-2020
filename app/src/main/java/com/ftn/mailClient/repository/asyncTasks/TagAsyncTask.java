package com.ftn.mailClient.repository.asyncTasks;

import com.ftn.mailClient.dao.TagDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.UserApi;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class TagAsyncTask {
    public static class FetchTagsAsyncTask extends LocalDatabaseCallbackAsyncTask<Long, Void, Boolean> {
        public FetchTagsAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
            TagDao tagDao = localDatabase.tagDao();
            UserApi userApi = RetrofitClient.getApi(UserApi.class);
            Long userId = longs[0];

            try {
                Response<List<Tag>> response = userApi.getUserTags(userId).execute();
                if(response.isSuccessful()){
                    List<Tag> tags = response.body();
                    tagDao.insertAll(tags);
                    return true;
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return false;
        }
    }

    public static class InsertTagAsyncTask extends LocalDatabaseCallbackAsyncTask<Tag, Void, Boolean>{
        private Long userId;

        public InsertTagAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long userId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(Tag... tags) {
            TagDao tagDao = localDatabase.tagDao();
            UserApi userApi = RetrofitClient.getApi(UserApi.class);
            Tag tag = tags[0];

            try {
                Response<Tag> response = userApi.addUserTag(userId, tag).execute();
                if(response.isSuccessful()){
                    tag = response.body();
                    tagDao.insert(tag);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
