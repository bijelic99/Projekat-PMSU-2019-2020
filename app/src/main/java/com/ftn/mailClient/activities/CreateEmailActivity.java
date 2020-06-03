package com.ftn.mailClient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;

public class CreateEmailActivity extends AppCompatActivity {

    EditText mEmailTo;
    EditText mCc;
    EditText mBcc;
    EditText mEmailSubject;
    EditText mTags;
    EditText mContent;

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

        mEmailTo = findViewById(R.id.editEmailTo);
        mCc = findViewById(R.id.editEmailCc);
        mBcc = findViewById(R.id.editEmailBcc);
        mEmailSubject = findViewById(R.id.editEmailSubject);
        mTags = findViewById(R.id.multiAutoCompleteTextViewTags);
        mContent = findViewById(R.id.editTextContent);

        Intent intent = new Intent(this, EmailsActivity.class);
        startActivity(intent);
    }

    //Disable back button
    /*
    @Override
    public void onBackPressed() { }
    */
}
