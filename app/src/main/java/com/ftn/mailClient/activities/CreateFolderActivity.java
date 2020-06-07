package com.ftn.mailClient.activities;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFolderActivity extends AppCompatActivity {

    private EditText mFolderName;
    private EditText mParentFolder;
    private String folderName;

    private Folder folder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        mFolderName = findViewById(R.id.editCreateFolderName);
        mParentFolder = findViewById(R.id.autoCompleteTextViewParentFolder);

        Serializable parentFolderSerializable = getIntent().getSerializableExtra("parent");

        folder = parentFolderSerializable != null ? (Folder) parentFolderSerializable : null;
        mParentFolder.setText(folder != null ? folder.getName() : "");

        Button b = findViewById(R.id.buttonForCreateFolder);
        b.setOnClickListener(v -> clickHandler(v));

        Button cancelBtn = findViewById(R.id.buttonForCancelCreateFolder);
        cancelBtn.setOnClickListener(v -> activityEndRedirect());
    }
    public void clickHandler(View v) {

        folderName = mFolderName.getText().toString();
        Folder newFolder = new Folder(null, folderName, folder != null ? new FolderMetadata(folder) : null, new ArrayList<>(), new ArrayList<>());

        FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
        Context currentContext = this;
        if(folder != null) {
            folderApi.saveFolder(newFolder).enqueue(new Callback<Folder>() {
                @Override
                public void onResponse(Call<Folder> call, Response<Folder> response) {
                    if (response.isSuccessful()) {
                        activityEndRedirect();
                    } else
                        Toast.makeText(currentContext, "There was a problem, folder isn't created " + response.code(), Toast.LENGTH_LONG);
                }

                @Override
                public void onFailure(Call<Folder> call, Throwable t) {
                    Toast.makeText(currentContext, "There was a problem, folder isn't created", Toast.LENGTH_LONG);
                }
            });
        }
        else {
            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.user_details_file_key),MODE_PRIVATE);
            if(sharedPreferences != null){
                Long accountId = sharedPreferences.getLong(getString(R.string.user_account_id), -5L);
                folderApi.addAccountFolder(accountId, newFolder).enqueue(new Callback<Folder>() {
                    @Override
                    public void onResponse(Call<Folder> call, Response<Folder> response) {
                        if (response.isSuccessful()) {
                            activityEndRedirect();
                        } else
                            Toast.makeText(currentContext, "There was a problem, folder isn't created " + response.code(), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(Call<Folder> call, Throwable t) {
                        Toast.makeText(currentContext, "There was a problem, folder isn't created", Toast.LENGTH_LONG);
                    }
                });
            }

        }


    }

    private void activityEndRedirect(){
        if(folder != null) {
            Intent intent = new Intent(getBaseContext(), FolderActivity.class);
            intent.putExtra("folder", getFolder());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getBaseContext(), FoldersActivity.class);
            startActivity(intent);
        }
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    //Disable back button
    /*
    @Override
    public void onBackPressed() { }
    */
}
