package com.ftn.mailClient.activities.contactActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.CreateContactActivity;
import com.ftn.mailClient.adapters.ContactRecyclerViewAdapter;
import com.ftn.mailClient.model.ContactMetadata;
import com.ftn.mailClient.model.Identifiable;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.ContactViewModel;

import java.util.stream.Collectors;

public class ContactActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ContactRecyclerViewAdapter contactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactRecyclerViewAdapter);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("contactId", -55l);
        if(id != -55l){
            contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
            contactViewModel.setContact(id);
            contactViewModel.getContact();

            SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                LiveData<FetchStatus> fetchStatusLiveData = contactViewModel.syncContact();
                fetchStatusLiveData.observe(this, new Observer<FetchStatus>() {
                    @Override
                    public void onChanged(FetchStatus fetchStatus) {
                        if(fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(getApplicationContext(), R.string.refreshError, Toast.LENGTH_SHORT).show();
                        if(fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)){
                            if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                            fetchStatusLiveData.removeObserver(this);
                        }
                    }
                });
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
