package com.ftn.mailClient.activities.folderActivity.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.folderActivity.FolderActivity;
import com.ftn.mailClient.adapters.FolderContentRecyclerViewAdapter;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FolderContentsFragment extends Fragment {

    public FolderContentsFragment() {
        // Required empty public constructor
    }

    private FolderContentRecyclerViewAdapter folderContentRecyclerViewAdapter;
    private Folder folder;

    public void setFolder(Folder folder) {
        this.folder = folder;

        folderContentRecyclerViewAdapter = new FolderContentRecyclerViewAdapter(getContext(), getFolderContents(folder));
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewFolderContent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(folderContentRecyclerViewAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_contents, container, false);



        return view;
    }



    /**
     * Povlaci listu foldera sa servera, kombinuje je sa listom foldera i tako vraca je kao listu.
     * Posto je ucitavanje foldera sa servera async zadatak oni nece odmah biti ucitani vec po izvrsenju callback-a.
     * Takodje obavestava FolderContentRecyclerViewAdapter kada se folderi ucitaju.
     * @param folder
     * @return Lista koja sadrzi foldere i poruke
     */
    private List<Object> getFolderContents(Folder folder) {
        List<Object> list = new ArrayList<>(folder.getMessages());
        FolderApi folderApi = RetrofitClient.<FolderApi>getApi(FolderApi.class);
        folderApi.getInnerFolders(folder.getId()).enqueue(new Callback<Set<Folder>>() {
            @Override
            public void onResponse(Call<Set<Folder>> call, Response<Set<Folder>> response) {
                if(response.isSuccessful()) {
                    list.addAll(response.body());
                    folderContentRecyclerViewAdapter.notifyDataSetChanged();
                }
                else Toast.makeText(getContext(), R.string.cant_get_folders, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Set<Folder>> call, Throwable t) {
                Log.e("folder-fetch-err", t.getMessage());
                Toast.makeText(getContext(), R.string.cant_get_folders, Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }
}
