package com.ftn.mailClient.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi extends Api {
    @GET("accounts")
    Call<List<com.ftn.mailClient.model.Account>> getAccounts();
}
