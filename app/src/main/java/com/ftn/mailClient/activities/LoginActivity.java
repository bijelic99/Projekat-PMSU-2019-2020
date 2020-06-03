package com.ftn.mailClient.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button b = findViewById(R.id.buttonLogin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler(v);
            }
        });

        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.user_details_file_key), MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //TODO Izmeniti da bude dinamicki
        editor.putLong(getString(R.string.user_account_id), 4L);
        editor.putLong(getString(R.string.user_id), 2L);
        editor.commit();

    }

    public void clickHandler(View v) {
        Intent intent = new Intent(this, EmailsActivity.class);
        startActivity(intent);
    }

    //Disable back button
    @Override
    public void onBackPressed() { }
}
