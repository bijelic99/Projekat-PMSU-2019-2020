package com.ftn.mailClient.activities.foldersActivity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.mailClient.R;
import com.ftn.mailClient.adapters.FoldersListRecyclerViewAdapter;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderElement;
import com.ftn.mailClient.model.Message;

import java.util.ArrayList;


public class FolderListFragment extends Fragment {

    public FolderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_list, container, false);
        //TODO dodati dynamic data
        /*
        Folder f1 = new Folder();
        f1.setName("First folder");
        ArrayList<FolderElement> messages = new ArrayList<>();
        messages.add(new Message());
        messages.add(new Message());
        f1.setFolderContents(messages);

        Folder f2 = new Folder();
        f2.setName("Second folder");

        ArrayList<Folder> folders = new ArrayList<>();
        folders.add(f1);
        folders.add(f2);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        FoldersListRecyclerViewAdapter foldersListRecyclerViewAdapter = new FoldersListRecyclerViewAdapter(getContext(), folders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(foldersListRecyclerViewAdapter);
        */
        return view;
    }
}
