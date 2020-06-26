package com.ftn.mailClient.retrofit;

import java.util.List;
import java.util.Map;

import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.utill.UserTokenWrapper;
import com.ftn.mailClient.model.User;

import retrofit2.Call;
import retrofit2.http.*;

public interface UserApi extends Api {
    @GET("api/users/{id}/accounts")
    Call<List<com.ftn.mailClient.model.Account>> getUsersAccounts(@Path("id") Long id);

    @GET("api/users/{id}/contacts")
    Call<List<Contact>> getUserContacts(@Path("id") Long id);

    @POST("api/users/{id}/contacts")
    Call<Contact> addUserContact(@Path("id") Long id, @Body Contact contact);

    @GET("api/users/{id}/tags")
    Call<List<Tag>> getUserTags(@Path("id") Long id);

    @POST("api/users/{id}/tags")
    Call<Tag> addUserTag(@Path("id") Long id, @Body Tag tag);

    @POST("/login")
    Call<UserTokenWrapper> login(@Body Map<String, String> credentials);
    @POST("api/users/{id}")
    Call<User> addUser(@Path("id") Long accountId, @Body User user);

    @POST("/register")
    Call<User> register(@Body User user);
}
