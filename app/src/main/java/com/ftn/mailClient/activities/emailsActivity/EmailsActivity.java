package com.ftn.mailClient.activities.emailsActivity;

import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.CreateEmailActivity;
import com.ftn.mailClient.adapters.EmailRecyclerViewAdapter;
import com.ftn.mailClient.dialogs.EmailFilterDialog;
import com.ftn.mailClient.navigationRouter.NavigationRouter;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.AccountEmailsViewModel;
import com.google.android.material.navigation.NavigationView;

public class EmailsActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private AccountEmailsViewModel accountEmailsViewModel;

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

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmailRecyclerViewAdapter adapter  = new EmailRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        accountEmailsViewModel = new ViewModelProvider(this).get(AccountEmailsViewModel.class);

        Context c = this;

        if(accountEmailsViewModel.getMessages() != null){
            accountEmailsViewModel.getMessages().observe(this, messages -> {
                if(messages != null)
                    adapter.setMessages(messages);
            });

            swipeRefreshLayout.setOnRefreshListener(() -> {
                LiveData<FetchStatus> fetchStatusLiveData = accountEmailsViewModel.syncMessages();
                fetchStatusLiveData.observe((LifecycleOwner) c, new Observer<FetchStatus>() {
                    @Override
                    public void onChanged(FetchStatus fetchStatus) {
                        if(fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(c, R.string.refreshError, Toast.LENGTH_SHORT).show();
                        if(fetchStatus.equals(FetchStatus.ERROR) || fetchStatus.equals(FetchStatus.DONE)) {
                            if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                            fetchStatusLiveData.removeObserver(this);
                        }

                    }
                });
            });
        }

        accountEmailsViewModel.getFilterEmailMutableLiveData().observe(this, filterEmail -> {
            if(filterEmail != null) {
                adapter.setFilterEmail(filterEmail);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
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
//        Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
        EmailFilterDialog dialog = new EmailFilterDialog(accountEmailsViewModel);
        dialog.show(getSupportFragmentManager(), "Email filter");

    }
    private void newEmailButtonClocked(){
        Intent intent = new Intent(this, CreateEmailActivity.class);
        this.startActivity(intent);
    }

}
