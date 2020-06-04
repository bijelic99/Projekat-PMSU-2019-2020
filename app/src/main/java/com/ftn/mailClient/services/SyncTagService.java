package com.ftn.mailClient.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.TagApi;

import java.util.HashMap;
import java.util.Map;

public class SyncTagService extends Service {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(intent.hasExtra("tagId")){
            Long tagId = intent.getLongExtra("tagId", -5L);
            String nameTag = intent.getStringExtra("name");

            if(intent.hasExtra("name")){
                nameTag = (String) intent.getSerializableExtra("name");
            }

            Map<String, Object> syncData = new HashMap<>();
            syncData.put("name", nameTag);

            TagApi tagApi = RetrofitClient.getApi(TagApi.class);
            tagApi.setTag(tagId, syncData);
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
