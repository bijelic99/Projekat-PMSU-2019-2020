package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;
import java.util.Set;

public interface AccountApi extends Api {
    @GET("api/accounts/{id}/folders")
    Call<List<FolderMetadata>> getAccountFolders(@Path("id") Long id);

    @GET("api/accounts/{id}")
    Call<Account> getAccount(@Path("id") Long id);
}
