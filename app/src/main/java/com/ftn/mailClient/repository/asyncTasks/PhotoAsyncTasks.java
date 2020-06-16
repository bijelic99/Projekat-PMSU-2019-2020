package com.ftn.mailClient.repository.asyncTasks;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.ftn.mailClient.model.Photo;
import com.ftn.mailClient.retrofit.PhotoApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.CallbackAsyncTask;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class PhotoAsyncTasks {
    public static class InsertPhotoAsyncTask extends CallbackAsyncTask<Bitmap, Void, Photo>{

        public InsertPhotoAsyncTask(OnPostExecuteFunctionFunctionalInterface<Photo> onPostExecuteFunctionFunctionalInterface) {
            super(onPostExecuteFunctionFunctionalInterface);
        }

        @Override
        protected Photo doInBackground(Bitmap... bitmaps) {
            Bitmap photo = bitmaps[0];
            PhotoApi photoApi = RetrofitClient.getApi(PhotoApi.class);
            try{
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageBytes = stream.toByteArray();
                stream.close();
                RequestBody requestFile =
                        RequestBody.create(imageBytes, MediaType.parse("image/png"));
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", "profile_image.png", requestFile);
                Response<Photo> response = photoApi.addPhotoToServer(body).execute();
                if(response.isSuccessful()){
                    return response.body();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
