package com.ftn.mailClient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ftn.mailClient.R;
import com.ftn.mailClient.adapters.TagRecyclerViewAdapter;
import com.ftn.mailClient.navigationRouter.NavigationRouter;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.AccountTagsViewModel;
import com.google.android.material.navigation.NavigationView;

public class TagsActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private AccountTagsViewModel accountTagsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        Toolbar toolbar = findViewById(R.id.tags_activity_toolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationView = findViewById(R.id.tags_nav_view);
        Context context = this;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(drawer != null) drawer.closeDrawer(Gravity.START);
                return NavigationRouter.routeFromMenuItem(context, menuItem);

            }
        });
        drawer = findViewById(R.id.tags_activity);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TagRecyclerViewAdapter adapter  = new TagRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        accountTagsViewModel = new ViewModelProvider(this).get(AccountTagsViewModel.class);

        Context c = this;

        if(accountTagsViewModel.getTags() != null) {
            accountTagsViewModel.getTags().observe(this, tags -> {
                if (tags != null)
                    adapter.setTags(tags);
            });

            swipeRefreshLayout.setOnRefreshListener(() -> {
                LiveData<FetchStatus> fetchStatusLiveData = accountTagsViewModel.syncTags();
                fetchStatusLiveData.observe((LifecycleOwner) c, new Observer<FetchStatus>() {
                    @Override
                    public void onChanged(FetchStatus fetchStatus) {
                        if (fetchStatus.equals(FetchStatus.ERROR))
                            Toast.makeText(c, R.string.refreshError, Toast.LENGTH_SHORT).show();
                        if (fetchStatus.equals(FetchStatus.ERROR) || fetchStatus.equals(FetchStatus.DONE)) {
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                            fetchStatusLiveData.removeObserver(this);
                        }

                    }
                });
            });
        }
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
        inflater.inflate(R.menu.tag_menu, menu);
        MenuItem filter = menu.findItem(R.id.tag_filter_menu_item);
        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                filterButtonClicked();
                return true;
            }
        });
        MenuItem newTag = menu.findItem(R.id.add_tag_menu_item);
        newTag.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                newTagButtonClicked();
                return true;
            }
        });

        return true;
    }

    private void filterButtonClicked(){
        Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
    }
    private void newTagButtonClicked(){
        Intent intent = new Intent(this, CreateTagActivity.class);
        this.startActivity(intent);
    }
}
