package com.ftn.mailClient.activities.folderActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Folder;

public class FolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Intent intent = getIntent();
        if(intent != null && intent.getSerializableExtra("folder") != null){
            Folder f = (Folder) intent.getSerializableExtra("folder");
            View view = findViewById(R.id.folder_data);
            TextView fName = view.findViewById(R.id.folder_name);
            fName.setText(f.getName());
            ((TextView)findViewById(R.id.folder_no_of_messages)).setText(getResources().getString(R.string.folder_no_of_messages, 2));
            ((TextView)findViewById(R.id.folder_no_of_files)).setText(getResources().getString(R.string.folder_no_of_files, 1));
        }
    }
}
