package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Photo;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PhotoApi extends Api {
    @Multipart
    @POST("api/images/")
    Call<Photo> addPhotoToServer(@Part MultipartBody.Part image);
}
