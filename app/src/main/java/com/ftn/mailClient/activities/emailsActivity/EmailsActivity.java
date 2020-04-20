package com.ftn.mailClient.activities.emailsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ftn.mailClient.R;
import com.ftn.mailClient.navigationRouter.NavigationRouter;
import com.google.android.material.navigation.NavigationView;

public class EmailsActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout
        .activity_emails);
        Toolbar toolbar = findViewById(R.id.emails_activity_toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.emails_nav_view);
        Context context = this;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(drawer != null) drawer.closeDrawer(Gravity.START);
                return NavigationRouter.routeFromMenuItem(context, menuItem);

            }
        });
        drawer = findViewById(R.id.emails_activity);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
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
