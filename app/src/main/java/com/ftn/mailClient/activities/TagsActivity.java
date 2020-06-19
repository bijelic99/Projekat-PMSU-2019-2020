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
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
    private AccountTagsViewModel tagsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        Toolbar toolbar = findViewById(R.id.tags_activity_toolbar);

        setSupportActionBar(toolbar);

        Context context = this;
        NavigationView navigationView = findViewById(R.id.tags_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(drawer != null) drawer.closeDrawer(Gravity.START);
                return NavigationRouter.routeFromMenuItem(context, menuItem);
            }
        });
        drawer = findViewById(R.id.tags_activity);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_drawer_open, R.string.nav_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        tagsViewModel = new ViewModelProvider(this).get(AccountTagsViewModel.class);


        RecyclerView recyclerView = findViewById(R.id.recycler_view_tag);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TagRecyclerViewAdapter tagRecyclerViewAdapter = new TagRecyclerViewAdapter(this);
        recyclerView.setAdapter(tagRecyclerViewAdapter);


        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_tag);
        swipeRefreshLayout.setRefreshing(true);
        fetchTags();
        swipeRefreshLayout.setOnRefreshListener(() -> fetchTags());
        tagsViewModel.getTags().observe(this, tags -> {
            if(tags != null)
                tagRecyclerViewAdapter.setTags(tags);
        });

    }

    private void fetchTags(){
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_tag);
        LiveData<FetchStatus> fetch = tagsViewModel.fetchTags();
        fetch.observe(this, new Observer<FetchStatus>() {
            @Override
            public void onChanged(FetchStatus fetchStatus) {
                if (fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(getApplicationContext(), R.string.refreshError, Toast.LENGTH_SHORT).show();
                if(fetchStatus.equals(FetchStatus.ERROR) || fetchStatus.equals(FetchStatus.DONE)) {
                    fetch.removeObserver(this);
                    if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tag_menu, menu);
        MenuItem addTag = menu.findItem(R.id.add_tag_menu_item);
        addTag.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addTagClicked();
                return true;
            }
        });

        return true;
    }

    private void addTagClicked() {
        Intent intent = new Intent(this, CreateTagActivity.class);
        this.startActivity(intent);
    }
}
