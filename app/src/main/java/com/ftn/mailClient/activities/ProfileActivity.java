package com.ftn.mailClient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ftn.mailClient.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button bu = findViewById(R.id.buttonLogout);
        bu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                clickHandler(v);
            }
        });
    }

    private void clickHandler(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
