package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Tag;

import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TagApi extends Api{

    @GET("api/tags/{id}")
    Call<Set<Tag>> getTag(@Path("id") Long id);

    @POST("api/tags")
    Call<Tag> saveTag(@Body Tag tag);

    @PUT("api/tags/{id}")
    Call<Tag> setTag(@Path("id") Long id, @Body Map<String, Object> data);
}
