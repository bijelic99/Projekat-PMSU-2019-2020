package com.ftn.mailClient.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.Serializable;
import java.time.LocalDate;
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
            folderApi.syncFolder(folderId, syncData).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if(response.isSuccessful()){
                        Map<String, Object> resData = response.body();
                        Intent i = new Intent();
                        i.putExtra("folders", (Serializable) response.body().get("folders"));
                        i.putExtra("messages", (Serializable) response.body().get("messages"));
                        String actionName = folderId+"_folderSync";
                        i.setAction(actionName);
                        sendBroadcast(i);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
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
