package com.ftn.mailClient.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.ftn.mailClient.model.Identifiable;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.FolderSyncWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncFolderService extends Service {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra("folderId")) {
            Long folderId = intent.getLongExtra("folderId", -5L);
            LocalDateTime latestMessageTimestamp = null;
            List<Long> folderList = new ArrayList<>();

            if (intent.hasExtra("latestMessageTimestamp"))
                latestMessageTimestamp = (LocalDateTime) intent.getSerializableExtra("latestMessageTimestamp");
            if (intent.hasExtra("folderList"))
                folderList = (List<Long>) intent.getSerializableExtra("folderList");

            Map<String, Object> syncData = new HashMap<>();
            syncData.put("latestMessageTimestamp", latestMessageTimestamp != null ? latestMessageTimestamp.format(DateTimeFormatter.ISO_DATE_TIME) : null);
            syncData.put("folderList", folderList);

            FolderApi folderApi = RetrofitClient.<FolderApi>getApi(FolderApi.class);
            folderApi.syncFolder(folderId, syncData).enqueue(new Callback<FolderSyncWrapper>() {
                     @Override
                     public void onResponse(Call<FolderSyncWrapper> call, Response<FolderSyncWrapper> response) {
                         if(response.isSuccessful()){
                             FolderSyncWrapper resData = response.body();
                             Intent i = new Intent();
                             i.putExtra("folders", (Serializable) resData.getFolders());
                             i.putExtra("messages", (Serializable) resData.getMessages());
                             String actionName = folderId+"_folderSync";
                             i.setAction(actionName);
                             sendBroadcast(i);
                         }
                     }

                     @Override
                     public void onFailure(Call<FolderSyncWrapper> call, Throwable t) {
                         Log.e("fetch-error", t.getMessage());
                     }
                 });
        }
        stopSelf();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
