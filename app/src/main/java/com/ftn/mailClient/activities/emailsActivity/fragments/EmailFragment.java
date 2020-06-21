package com.ftn.mailClient.activities.emailsActivity.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.EmailActivity;

public class EmailFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false);
    }

    private void emailClicked(){
        Intent intent = new Intent(getContext(), EmailActivity.class);
        getContext().startActivity(intent);
    }
}
