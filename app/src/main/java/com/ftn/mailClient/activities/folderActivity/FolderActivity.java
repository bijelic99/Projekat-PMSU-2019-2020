package com.ftn.mailClient.activities.folderActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.CreateFolderActivity;
import com.ftn.mailClient.adapters.FolderContentRecyclerViewAdapter;
import com.ftn.mailClient.model.Identifiable;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.FolderViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class FolderActivity extends AppCompatActivity {
    private FolderViewModel folderViewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        TextView noOfMsg = findViewById(R.id.folder_no_of_messages);
        TextView noOfFiles = findViewById(R.id.folder_no_of_files);

        FolderContentRecyclerViewAdapter folderContentRecyclerViewAdapter = new FolderContentRecyclerViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(folderContentRecyclerViewAdapter);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("folderId", -55l);
        if(id != -55l){
            folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
            folderViewModel.setFolder(id);
            folderViewModel.getFolder().observe(this, folderMetadata -> {
                noOfMsg.setText(getString(R.string.folder_no_of_messages, folderMetadata.getNumberOfMessages()));
                noOfFiles.setText(getString(R.string.folder_no_of_files, folderMetadata.getNumberOfFolders()));
            });
            folderViewModel.getMessages().observe(this, messages -> {
                if(messages != null)
                folderContentRecyclerViewAdapter.add( messages.stream()
                        .map(message -> (Identifiable) message)
                        .collect(Collectors.toList()));
            });

            folderViewModel.getFolders().observe(this, folderMetadata -> {
                if(folderMetadata != null)
                    folderContentRecyclerViewAdapter.add(folderMetadata.stream()
                        .map(folderMetadata1 -> (Identifiable) folderMetadata1)
                        .collect(Collectors.toList()));
            });

            SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                LiveData<FetchStatus> fetchStatusLiveData = folderViewModel.syncFolder();
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
        intent.putExtra("parentId", folderViewModel.getFolderId());
        this.startActivity(intent);
    }

}
