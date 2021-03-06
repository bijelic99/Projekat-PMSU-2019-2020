package com.ftn.mailClient.activities.foldersActivity;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.lifecycle.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.CreateFolderActivity;
import com.ftn.mailClient.adapters.FoldersListRecyclerViewAdapter;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.navigationRouter.NavigationRouter;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.AccountViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FoldersActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private AccountViewModel accountViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.folders_activity);


        accountViewModel =new ViewModelProvider(this).get(AccountViewModel.class);
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.user_details_file_key), MODE_PRIVATE);
        if(sharedPreferences.contains(getString(R.string.user_account_id))){
            Long currentAccountId = sharedPreferences.getLong(getString(R.string.user_account_id), -99);
            accountViewModel.setAccount(currentAccountId);
        }

        Context context = this;
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(drawer != null) drawer.closeDrawer(Gravity.START);
                return NavigationRouter.routeFromMenuItem(context, menuItem);
            }
        });



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        FoldersListRecyclerViewAdapter adapter = new FoldersListRecyclerViewAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        accountViewModel.getAccountFolders().observe(this, folders -> {
            adapter.setFolders(folders);
        });


        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            LiveData<FetchStatus> statusLiveData = accountViewModel.syncAccountFolders();
            statusLiveData.observe(this, new Observer<FetchStatus>() {
                @Override
                public void onChanged(FetchStatus fetchStatus) {
                    if(fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(getApplicationContext(), R.string.refreshError, Toast.LENGTH_SHORT).show();
                    if(fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)){
                        swipeRefreshLayout.setRefreshing(false);
                        statusLiveData.removeObserver(this);
                    }
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.folders_activity_menu, menu);
        MenuItem addFolder = menu.findItem(R.id.add_folder_menu_item);
        addFolder.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addFolderClicked();
                return true;
            }
        });

        return true;
    }

    private void addFolderClicked() {
        Intent intent = new Intent(this, CreateFolderActivity.class);
        this.startActivity(intent);
    }




}
