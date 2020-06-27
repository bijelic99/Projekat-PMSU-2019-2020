package com.ftn.mailClient.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.ftn.mailClient.R;
import com.ftn.mailClient.authorization.UserAccountInfo;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.AccountRepository;

import java.util.Timer;
import java.util.TimerTask;

public class FetchMailsService extends Service {

    public static final int notify = 10;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public void onCreate() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean sync = sharedPreferences.getBoolean(getString(R.string.pref_sync), false);

        Toast.makeText(getApplicationContext(),"Started FetchEmailsService", Toast.LENGTH_LONG).show();

        if(sync){
            int interval = sharedPreferences.getInt(getString(R.string.pref_sync_list), notify);

            if (mTimer != null) // Cancel if already existed
                mTimer.cancel();
            else
                mTimer = new Timer(); // recreate new
            mTimer.scheduleAtFixedRate(new TimeDisplay(new AccountRepository(getApplication()), this), 0, interval * 60 * 1000);
        }

         // Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {

        private AccountRepository accountRepository;
        private Context context;

        public TimeDisplay(AccountRepository accountRepository, Context context) {
            this.accountRepository = accountRepository;
            this.context = context;
        }

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    UserAccountInfo userAccountInfo = UserAccountInfo.getUserAccountInfo();

                    userAccountInfo.loginIfAvailable(context);
                    if (userAccountInfo.getLoggedIn() && userAccountInfo.getSelectedAccountId() != null) {
                        accountRepository.fetchAccountMessages(userAccountInfo.getSelectedAccountId());
                        Toast.makeText(getApplicationContext(),"Started FetchEmailsService", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
