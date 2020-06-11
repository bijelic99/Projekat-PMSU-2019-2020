package com.ftn.mailClient.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.folderActivity.FolderActivity;
import com.ftn.mailClient.activities.foldersActivity.FoldersActivity;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.CreateFolderViewModel;
import com.ftn.mailClient.viewModel.FolderViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFolderActivity extends AppCompatActivity {

    private EditText mFolderName;
    private EditText mParentFolder;
    private String folderName;
    private CreateFolderViewModel createFolderViewModel;
    private ProgressBar progressBar;
    private Button insertButton;
    private Button cancelBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        mFolderName = findViewById(R.id.editCreateFolderName);
        mParentFolder = findViewById(R.id.autoCompleteTextViewParentFolder);
        progressBar = findViewById(R.id.progress_bar);

        createFolderViewModel = new ViewModelProvider(this).get(CreateFolderViewModel.class);

        Long parentId = getIntent().getLongExtra("parentId", -55L);

        if(parentId != -55L){
            createFolderViewModel.setParentFolder(parentId);
            createFolderViewModel.getParentFolder().observe(this, folderMetadata -> {
                if(folderMetadata != null)
                    mParentFolder.setText(folderMetadata.getName());
                else
                    mParentFolder.setText(R.string.no_parent_folder);
            });
        }
        else {
            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.user_details_file_key), MODE_PRIVATE);
            if(sharedPreferences.contains(getString(R.string.user_account_id))){
                Long currentAccountId = sharedPreferences.getLong(getString(R.string.user_account_id), -99);
                createFolderViewModel.setAccountId(currentAccountId);
                mParentFolder.setText(R.string.no_parent_folder);
            }
            else throw new NullPointerException("User needs to have an account to create account folder");
        }



        insertButton = findViewById(R.id.buttonForCreateFolder);
        insertButton.setOnClickListener(v -> clickHandler(v));

        cancelBtn = findViewById(R.id.buttonForCancelCreateFolder);
        cancelBtn.setOnClickListener(v -> activityEndRedirect());
    }
    public void clickHandler(View v) {
        try {
            insertButton.setEnabled(false);
            cancelBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            createFolderViewModel.setFolderName(mFolderName.getText().toString());

            createFolderViewModel.addNewFolder().observe(this, fetchStatus -> {
                if(fetchStatus.equals(FetchStatus.ERROR)) {
                    Toast.makeText(this, R.string.create_error, Toast.LENGTH_SHORT).show();
                    insertButton.setEnabled(true);
                    cancelBtn.setEnabled(true);
                }
                if(fetchStatus.equals(FetchStatus.DONE)) {
                    activityEndRedirect();
                }
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setIndeterminate(false);
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void activityEndRedirect(){
        if(!createFolderViewModel.isAccountFolder()) {
            Intent intent = new Intent(getBaseContext(), FolderActivity.class);
            intent.putExtra("folderId", createFolderViewModel.getParentFolderId());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getBaseContext(), FoldersActivity.class);
            startActivity(intent);
        }
    }


    //Disable back button
    /*
    @Override
    public void onBackPressed() { }
    */
}
