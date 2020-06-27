package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;
import java.util.Set;

public interface AccountApi extends Api {
    @GET("api/accounts/{id}/folders")
    Call<List<FolderMetadata>> getAccountFolders(@Path("id") Long id);

    @GET("api/accounts/{id}")
    Call<Account> getAccount(@Path("id") Long id);

    @GET("api/accounts/{id}/messages")
    Call<List<Message>> getAccountMessages(@Path("id") Long id);

    @GET("api/accounts/{id}/tags")
    Call<List<Tag>> getAccountTags(@Path("id") Long id);

    @GET("api/users/{id}/accounts")
    Call<List<Account>> getUserAccounts(@Path("id") Long userId);

    @POST("api/users/{id}/accounts")
    Call<Account> addUserAccount(@Path("id") Long userId, @Body Account account);
}
