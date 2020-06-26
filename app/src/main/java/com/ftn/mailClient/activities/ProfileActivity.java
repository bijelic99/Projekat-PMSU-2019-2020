package com.ftn.mailClient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.R;
import com.ftn.mailClient.authorization.UserAccountInfo;
import com.ftn.mailClient.dialogs.AddRuleDialog;
import com.ftn.mailClient.dialogs.NewUserDialog;

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
                logout();
                break;
            }
            case R.id.buttonChange:{
                NewUserDialog newUserDialog = new NewUserDialog();
                newUserDialog.show(getSupportFragmentManager(), "Add profile dialog");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        LiveData<Boolean> liveData = UserAccountInfo.getUserAccountInfo().logOut(getApplication());
        Context context = this;
        liveData.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    liveData.removeObserver(this);
                }
            }
        });

    }
}
