package com.ftn.mailClient.activities.folderActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.folderActivity.fragments.FolderContentsFragment;
import com.ftn.mailClient.model.Folder;

public class FolderActivity extends AppCompatActivity {
    private Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Intent intent = getIntent();
        if(intent != null && intent.getSerializableExtra("folder") != null){
            folder = (Folder) intent.getSerializableExtra("folder");
            FolderContentsFragment folderContentsFragment = (FolderContentsFragment) getSupportFragmentManager().findFragmentById(R.id.folder_contents);
            folderContentsFragment.setFolder(folder);

            View view = findViewById(R.id.folder_data);
            TextView fName = view.findViewById(R.id.folder_name);
            fName.setText(folder.getName());

            ((TextView)findViewById(R.id.folder_no_of_messages)).setText(getResources().getString(R.string.folder_no_of_messages, folder.getMessages().size()));
            ((TextView)findViewById(R.id.folder_no_of_files)).setText(getResources().getString(R.string.folder_no_of_files, folder.getFolders().size()));

            IntentFilter intentFilter = new IntentFilter(folder.getId()+"_folderSync");
            this.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onDestroy() {
        try{
            this.unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(folder != null){
                ((TextView)findViewById(R.id.folder_no_of_messages)).setText(getResources().getString(R.string.folder_no_of_messages, folder.getMessages().size()));
                ((TextView)findViewById(R.id.folder_no_of_files)).setText(getResources().getString(R.string.folder_no_of_files, folder.getFolders().size()));
            }
            else {
                ((TextView)findViewById(R.id.folder_no_of_messages)).setText(getResources().getString(R.string.folder_no_of_messages, 0));
                ((TextView)findViewById(R.id.folder_no_of_files)).setText(getResources().getString(R.string.folder_no_of_files, 0));
            }
        }
    };
}
