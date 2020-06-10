package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Folder;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.Set;

public interface AccountApi extends Api {
    @GET("api/accounts/{id}/folders")
    Call<Set<Folder>> getAccountFolders(@Path("id") Long id);
}
