package com.ftn.mailClient.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ftn.mailClient.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash_screen);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_screen);
        final Context c = this;
        isNetworkAvailable();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(c, MainActivity.class);
                startActivity(intent);
            }
        }, 5000);
        finish();
    }

    protected void isNetworkAvailable() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Log.d("*************MYLOG************", (networkInfo != null && networkInfo.isConnected()) + "");
    }
}
