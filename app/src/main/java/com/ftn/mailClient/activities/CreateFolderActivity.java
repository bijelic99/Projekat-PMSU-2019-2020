package com.ftn.mailClient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.folderActivity.FolderActivity;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;

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

        folder = (Folder) this.getIntent().getExtras().get("parent");
        mParentFolder.setText(folder.getName());

        Button b = findViewById(R.id.buttonForCreateFolder);
        b.setOnClickListener(v -> clickHandler(v));
    }
    public void clickHandler(View v) {

        folderName = mFolderName.getText().toString();
        Folder newFolder = new Folder(null, folderName, folder.getId(), new HashSet<Long>(), new HashSet<Message>());

        FolderApi folderApi = RetrofitClient.getApi(FolderApi.class);
        folderApi.saveFolder(newFolder).enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                if(response.isSuccessful()) {
                    Intent intent = new Intent(getBaseContext(), FolderActivity.class);
                    intent.putExtra("folder", getFolder());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
                Toast.makeText(getBaseContext(), "There was a problem, folder isn't created", Toast.LENGTH_LONG);
            }
        });


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
