package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageApi extends Api {
    @POST("api/messages/send")
    Call<Message> sendMessage(@Body Message message);
}
