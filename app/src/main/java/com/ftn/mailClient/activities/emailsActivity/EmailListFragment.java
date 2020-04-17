package com.ftn.mailClient.activities.emailsActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.mailClient.R;
import com.ftn.mailClient.adapters.EmailRecyclerViewAdapter;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Message;


public class EmailListFragment extends Fragment {


    public EmailListFragment() {
        // Required empty public constructor
    }


    public static EmailListFragment newInstance() {
        EmailListFragment fragment = new EmailListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_email_list, container, false);

        Message message1 = new Message();
        message1.setSubject("dasdsada");
        message1.setContent("dasdadasad");
        Contact c = new Contact();
        c.setFirstName("A");
        c.setLastName("B");
        message1.setFrom(c);

        Message message2 = new Message();
        message2.setSubject("Subject");
        message2.setContent("Lorem ipsum");
        Contact c2 = new Contact();
        c2.setFirstName("C");
        c2.setLastName("D");
        message2.setFrom(c);

        Message[] messages = {message1, message2};

        RecyclerView recyclerView = inflate.findViewById(R.id.emails_list);
        EmailRecyclerViewAdapter emailRecyclerViewAdapter = new EmailRecyclerViewAdapter(getContext(), messages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(emailRecyclerViewAdapter);
        return inflate;
    }
}
