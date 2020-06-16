package com.ftn.mailClient.retrofit;

import java.util.List;

import com.ftn.mailClient.model.Contact;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserApi extends Api {
    @GET("api/users/{id}/accounts")
    Call<List<com.ftn.mailClient.model.Account>> getUsersAccounts(@Path("id") Long id);

    @GET("api/users/{id}/contacts")
    Call<List<Contact>> getUserContacts(@Path("id") Long id);

    @POST("api/users/{id}/contacts")
    Call<Contact> addUserContact(@Path("id") Long id, @Body Contact contact);
}
