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
import com.ftn.mailClient.utill.FolderContentsComparatorInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FolderContentsFragment extends Fragment {

    public FolderContentsFragment() {
        // Required empty public constructor
    }

    private FolderContentRecyclerViewAdapter folderContentRecyclerViewAdapter;
    private Folder folder;
    private SwipeRefreshLayout swipeRefreshLayout;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFolder(Folder folder) {
        this.folder = folder;
        List<Object> folderContents = new ArrayList<>(folder.getMessages());

        Intent intent = new Intent(getContext(), SyncFolderService.class);
        intent.putExtra("folderId", folder.getId());
        intent.putExtra("latestMessageTimestamp", folder.getMessages().stream()
                .map(message -> message.getDateTime())
                .max((o1, o2) -> o1.isAfter(o2) ? 1 : o1.isBefore(o2) ? -1 : 0)
                .orElse(null));
        intent.putExtra("folderList", new ArrayList<>(folder.getFolders()));
        swipeRefreshLayout.setRefreshing(true);
        getContext().startService(intent);

        folderContentRecyclerViewAdapter = new FolderContentRecyclerViewAdapter(getContext(), folderContents);
        Collections.sort(folderContentRecyclerViewAdapter.getContents(), FolderContentsComparatorInterface::folderContentsComparator);
        folderContentRecyclerViewAdapter.notifyDataSetChanged();
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
                Collections.sort(folderContentRecyclerViewAdapter.getContents(), FolderContentsComparatorInterface::folderContentsComparator);
                folderContentRecyclerViewAdapter.notifyDataSetChanged();
            }
            if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        }
    };



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
