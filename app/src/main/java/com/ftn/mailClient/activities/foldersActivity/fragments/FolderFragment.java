package com.ftn.mailClient.activities.foldersActivity.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.folderActivity.FolderActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FolderFragment extends Fragment {

    public FolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        return view;
    }



    private void folderClicked(){
        Log.d("aaa", "clckd");
        Intent intent = new Intent(getContext(), FolderActivity.class);
        getContext().startActivity(intent);
    }
}
