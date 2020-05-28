package com.ftn.mailClient.activities.folderActivity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.mailClient.R;
import com.ftn.mailClient.adapters.FolderContentRecyclerViewAdapter;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderElement;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;

import java.util.ArrayList;


public class FolderContentsFragment extends Fragment {

    public FolderContentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_contents, container, false);

        //TODO Change data to dynamic data
        /*
        Tag tag = new Tag(null, "Work");
        Tag tag1 = new Tag(null, "Important");
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(tag);
        tags.add(tag1);

        com.ftn.mailClient.model.Message message1 = new com.ftn.mailClient.model.Message();
        message1.setSubject("dasdsada");
        message1.setContent("dasdadasad");
        Contact c = new Contact();
        c.setFirstName("A");
        c.setLastName("B");
        message1.setFrom(c);
        message1.setTags(tags);

        com.ftn.mailClient.model.Message message2 = new com.ftn.mailClient.model.Message();
        message2.setSubject("Subject");
        message2.setContent("Lorem ipsum");
        Contact c2 = new Contact();
        c2.setFirstName("C");
        c2.setLastName("D");
        message2.setFrom(c);
        message2.setTags(tags);

        Folder folder = new Folder(0, null, "Folder", null);

        ArrayList<FolderElement> content = new ArrayList<>();
        content.add(message1);
        content.add(message2);
        content.add(folder);

        FolderContentRecyclerViewAdapter folderContentRecyclerViewAdapter = new FolderContentRecyclerViewAdapter(getContext(), content);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFolderContent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(folderContentRecyclerViewAdapter);
        */
        return view;
    }
}
