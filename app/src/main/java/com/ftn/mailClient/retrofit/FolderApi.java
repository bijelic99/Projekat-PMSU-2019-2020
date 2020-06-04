package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.utill.FolderSyncWrapper;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;
import java.util.Set;

public interface FolderApi extends Api{
    @GET("api/folders/{id}/innerFolders")
    Call<Set<Folder>> getInnerFolders(@Path("id") Long id);

    @PUT("api/folders/{id}/sync")
    Call<FolderSyncWrapper> syncFolder(@Path("id") Long id, @Body Map<String, Object> data);

    @POST("api/accounts/{id}/folder")
    Call<Folder> addAccountFolder(@Path("id") Long accountId, @Body Folder folder);
}
