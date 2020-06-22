package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Contact;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ContactApi extends Api{

    @GET("api/contacts/{id}")
    Call<Contact> getContact(@Path("id") Long id);

    @GET("api/contacts/{id}/sync")
    Call<Contact> syncContact(@Path("id") Long id);
}
