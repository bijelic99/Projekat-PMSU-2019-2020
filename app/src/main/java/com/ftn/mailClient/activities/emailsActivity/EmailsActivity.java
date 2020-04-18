package com.ftn.mailClient.activities.emailsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ftn.mailClient.R;

public class EmailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout
        .activity_emails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.emails_menu, menu);
        MenuItem filter = menu.findItem(R.id.email_filter_menu_item);
        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                filterButtonClicked();
                return true;
            }
        });
        MenuItem newEmail = menu.findItem(R.id.new_email_menu_item);
        newEmail.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                newEmailButtonClocked();
                return true;
            }
        });

        return true;
    }

    private void filterButtonClicked(){
        Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
    }
    private void newEmailButtonClocked(){
        Toast.makeText(this, "Email", Toast.LENGTH_SHORT).show();
    }
}
