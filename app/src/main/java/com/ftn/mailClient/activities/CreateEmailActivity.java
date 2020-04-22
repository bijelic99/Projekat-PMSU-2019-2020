package com.ftn.mailClient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;

public class CreateEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email);

        Button b = findViewById(R.id.buttonCreateEmail);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler(v);
            }
        });
    }

    public void clickHandler(View v) {
        Intent intent = new Intent(this, EmailsActivity.class);
        startActivity(intent);
    }

    //Disable back button
    @Override
    public void onBackPressed() { }
}
