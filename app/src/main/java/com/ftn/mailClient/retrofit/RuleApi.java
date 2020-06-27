package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Rule;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RuleApi extends Api {
    @POST("api/accounts/{id}/rules")
    Call<Rule> addRule(@Path("id") Long accountId, @Body Rule rule);
}
