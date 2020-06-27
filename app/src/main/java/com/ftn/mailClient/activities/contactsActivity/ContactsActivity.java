package com.ftn.mailClient.activities.contactsActivity;

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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.CreateContactActivity;
import com.ftn.mailClient.adapters.ContactRecyclerViewAdapter;
import com.ftn.mailClient.navigationRouter.NavigationRouter;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.AccountEmailsViewModel;
import com.ftn.mailClient.viewModel.ContactsViewModel;
import com.google.android.material.navigation.NavigationView;

public class ContactsActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private ContactsViewModel contactsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        
        setSupportActionBar(toolbar);

        Context context = this;
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(drawer != null) drawer.closeDrawer(Gravity.START);
                return NavigationRouter.routeFromMenuItem(context, menuItem);
            }
        });
        drawer = findViewById(R.id.contacts_activity);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ContactRecyclerViewAdapter contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this);
        recyclerView.setAdapter(contactRecyclerViewAdapter);


        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(true);
        fetchContacts();
        swipeRefreshLayout.setOnRefreshListener(() -> fetchContacts());
        contactsViewModel.getContacts().observe(this, contacts -> {
            if(contacts != null)
                contactRecyclerViewAdapter.setContacts(contacts);
        });

    }

    private void fetchContacts(){
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        LiveData<FetchStatus> fetch = contactsViewModel.fetchContacts();
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
        inflater.inflate(R.menu.contacts_menu, menu);
        MenuItem addContact = menu.findItem(R.id.add_contact_menu_item);
        addContact.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addContactClicked();
                return true;
            }
        });
        
        return true;
    }

    private void addContactClicked() {
        Intent intent = new Intent(this, CreateContactActivity.class);
        this.startActivity(intent);
    }
}
