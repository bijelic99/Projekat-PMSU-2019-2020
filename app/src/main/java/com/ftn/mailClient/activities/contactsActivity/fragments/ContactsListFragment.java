package com.ftn.mailClient.activities.contactsActivity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.mailClient.R;
import com.ftn.mailClient.adapters.ContactRecyclerViewAdapter;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Photo;

import java.util.ArrayList;


public class ContactsListFragment extends Fragment {


    public ContactsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        Contact c1 = new Contact();
        c1.setDisplayName("John Doe");
        c1.setEmail("john.doe@mail.com");

        Contact c2 = new Contact();
        c2.setDisplayName("John Doe");
        c2.setEmail("john.doe@mail.com");

        ArrayList<Contact> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);

        //TODO dodaj ucitavanje

        RecyclerView recyclerView = view.findViewById(R.id.contacts_recycler_view);

        ContactRecyclerViewAdapter recyclerViewAdapter = new ContactRecyclerViewAdapter(getContext(), list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);


        return view;
    }
}
