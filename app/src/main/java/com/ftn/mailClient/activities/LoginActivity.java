package com.ftn.mailClient.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.EmailViewModel;
import com.ftn.mailClient.viewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.editUsername);
        passwordEditText = findViewById(R.id.editPassword);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.setUsername(s.toString());
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.setPassword(s.toString());
            }
        });

        Button b = findViewById(R.id.buttonLogin);
        b.setOnClickListener(v -> {
            LiveData<FetchStatus> liveData = loginViewModel.login();
            Context context = this;
            liveData.observeForever(new Observer<FetchStatus>() {
                @Override
                public void onChanged(FetchStatus fetchStatus) {
                    if(fetchStatus.equals(FetchStatus.DONE)) {
                        Intent intent = new Intent(context, EmailsActivity.class);
                        context.startActivity(intent);
                    }
                    if(fetchStatus.equals(FetchStatus.ERROR)){
                        Toast.makeText(context, R.string.loggin_failed, Toast.LENGTH_SHORT).show();
                    }
                    if(fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)) liveData.removeObserver(this);
                }
            });
        });



    }



    //Disable back button
    @Override
    public void onBackPressed() { }
}
