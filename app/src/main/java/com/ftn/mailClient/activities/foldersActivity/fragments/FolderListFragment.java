package com.ftn.mailClient.activities.foldersActivity.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;
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
import com.ftn.mailClient.retrofit.AccountApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class FolderListFragment extends Fragment {

    private Set<Folder> accountFolders = new HashSet<>();
    private FoldersListRecyclerViewAdapter foldersListRecyclerViewAdapter;


    public FolderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_list, container, false);
        //TODO dodati dynamic data

        fetchFolders();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        foldersListRecyclerViewAdapter = new FoldersListRecyclerViewAdapter(getContext(), accountFolders.stream().collect(Collectors.toList()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(foldersListRecyclerViewAdapter);




        return view;
    }

    private void fetchFolders(){
        AccountApi api = RetrofitClient.<AccountApi>getApi(AccountApi.class);
        Long accountId = null;
        try {
            SharedPreferences sharedPreferences  = getContext().getSharedPreferences(getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
            accountId = sharedPreferences.getLong(getString(R.string.user_account_id), -1);
        } catch (Exception e){
            e.printStackTrace();
        }


        api.getAccountFolders(accountId).enqueue(new Callback<Set<Folder>>() {
            @Override
            public void onResponse(Call<Set<Folder>> call, Response<Set<Folder>> response) {
                if(response.code() == 200){
                    accountFolders = response.body();
                    foldersListRecyclerViewAdapter.setFolders(accountFolders.stream().collect(Collectors.toList()));
                    foldersListRecyclerViewAdapter.notifyDataSetChanged();
                }
                else Toast.makeText( getContext(),"Cannot get account folders", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Set<Folder>> call, Throwable t) {
                Log.d("fetch-fail", t.getMessage());
                Toast.makeText( getContext(),"Cannot get account folders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
