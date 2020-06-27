package com.ftn.mailClient.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.authorization.AuthorizationInterceptor;
import com.ftn.mailClient.authorization.UserAccountInfo;
import com.ftn.mailClient.services.FetchMailsService;

public class SplashScreen extends AppCompatActivity {

    private Context context = getBaseContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                isNetworkAvailable();
            }
        }, 2500);
    }

    protected void isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            /*Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);*/
            Intent intent = null;
            if(UserAccountInfo.getUserAccountInfo().loginIfAvailable(this)) {
                intent = new Intent(this, EmailsActivity.class);
                if (!isMyServiceRunning(FetchMailsService.class)) {
                    context.startService(new Intent(context, FetchMailsService.class));
                }
            }
            else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
        } else {
            Toast toast=Toast.makeText(getApplicationContext(),"You are not connected to internet",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
