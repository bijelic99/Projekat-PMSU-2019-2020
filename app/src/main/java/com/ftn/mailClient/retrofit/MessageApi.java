package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageApi extends Api {
    @POST("api/messages/send")
    Call<Message> sendMessage(@Body Message message);

    @POST("api/accounts/{id}/drafts")
    Call<Message> addToDrafts(@Path("id") Long accountId, @Body Message message);

    @GET("api/messages/{id}")
    Call<Message> getMessage(@Path("id") Long id);

    @GET("api/messages/{id}/sync")
    Call<Message> syncMessage(@Path("id") Long id);
}
