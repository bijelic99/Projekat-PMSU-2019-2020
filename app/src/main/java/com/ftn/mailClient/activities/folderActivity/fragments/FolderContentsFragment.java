package com.ftn.mailClient.activities.folderActivity.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.ftn.mailClient.R;
import com.ftn.mailClient.adapters.FolderContentRecyclerViewAdapter;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.retrofit.FolderApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.services.SyncFolderService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FolderContentsFragment extends Fragment {

    public FolderContentsFragment() {
        // Required empty public constructor
    }

    private FolderContentRecyclerViewAdapter folderContentRecyclerViewAdapter;
    private Folder folder;
    private SwipeRefreshLayout swipeRefreshLayout;


    public void setFolder(Folder folder) {
        this.folder = folder;

        folderContentRecyclerViewAdapter = new FolderContentRecyclerViewAdapter(getContext(), getFolderContents(folder));
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewFolderContent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(folderContentRecyclerViewAdapter);

        IntentFilter intentFilter = new IntentFilter(folder.getId()+"_folderSync");
        getContext().registerReceiver(contentReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_contents, container, false);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(folder != null) {
                    Intent intent = new Intent(getContext(), SyncFolderService.class);
                    intent.putExtra("folderId", folder.getId());
                    intent.putExtra("latestMessageTimestamp", folder.getMessages().stream()
                            .map(message -> message.getDateTime())
                            .max((o1, o2) -> o1.isAfter(o2) ? 1 : o1.isBefore(o2) ? -1 : 0)
                            .orElse(null));
                    intent.putExtra("folderList", (Serializable) new ArrayList<>(folder.getFolders()));
                    getContext().startService(intent);


                }
                else swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    BroadcastReceiver contentReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Message> messages = (List<Message>) intent.getExtras().getSerializable("messages");
            List<Folder> folders = (List<Folder>) intent.getExtras().getSerializable("folders");

            folder.getMessages().addAll(messages);
            folder.getFolders().addAll(folders.stream().map(folder1 -> folder1.getId()).collect(Collectors.toList()));

            if(folderContentRecyclerViewAdapter != null){
                folderContentRecyclerViewAdapter.getContents()
                        .addAll(Stream.concat(messages.stream(), folders.stream()).collect(Collectors.toList()));
                folderContentRecyclerViewAdapter.notifyDataSetChanged();
            }
            if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        }
    };

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

    @Override
    public void onDestroyView() {
        try {
            getContext().unregisterReceiver(contentReceiver);
        }catch(Exception e) {
            e.printStackTrace();
        }

        super.onDestroyView();
    }
}
