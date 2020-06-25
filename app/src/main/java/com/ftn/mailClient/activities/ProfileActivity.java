package com.ftn.mailClient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ftn.mailClient.R;
import com.ftn.mailClient.dialogs.AddRuleDialog;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout drawer;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button bu = findViewById(R.id.buttonLogout);

        Button addRuleBtn = findViewById(R.id.add_rule_button);
        addRuleBtn.setOnClickListener(v -> {
            AddRuleDialog addRuleDialog = new AddRuleDialog();
            addRuleDialog.show(getSupportFragmentManager(), "Add rule dialog");});

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.buttonLogout:{
                goToLogin();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }
}
