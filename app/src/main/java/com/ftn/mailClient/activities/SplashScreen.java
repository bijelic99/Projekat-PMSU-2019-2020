package com.ftn.mailClient.activities;

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

public class SplashScreen extends AppCompatActivity {

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
}
