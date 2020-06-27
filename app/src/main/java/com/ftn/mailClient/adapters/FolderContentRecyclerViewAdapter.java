package com.ftn.mailClient.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.*;
import com.ftn.mailClient.utill.FolderContentsComparatorInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolderContentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Identifiable> contents;

    Context context;



    public FolderContentRecyclerViewAdapter(Context context){
        this.context = context;
        contents = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(List<Identifiable> identifiables){
        contents.addAll(identifiables);
        contents = Stream.concat(contents.stream(), identifiables.stream())
                .distinct().sorted(FolderContentsComparatorInterface::folderContentsComparator)
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //TODO namestiti ovo da radi sa novim modelom
        //return contents.get(position).getClass() == Message.class ? contentTypes.EMAIL.ordinal() : contentTypes.FOLDER.ordinal();
        return contents.get(position).getClass() == Message.class ? contentTypes.EMAIL.ordinal() : contentTypes.FOLDER.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        contentTypes enumViewType = contentTypes.values()[viewType];
        switch (enumViewType){
            case EMAIL:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_email, parent, false);
                viewHolder = new EmailRecyclerViewAdapter.EmailViewHolder(view);
                break;
            }
            case FOLDER:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_folder, parent, false);
                viewHolder = new FoldersListRecyclerViewAdapter.FolderViewHolder(view);
                break;
            }
            default: {
                Log.e("ViewHolder", "No viewHolder found");
                viewHolder = null;
            }
        }
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        contentTypes enumViewType = contentTypes.values()[getItemViewType(position)];
        switch (enumViewType){
            case EMAIL:{
                EmailRecyclerViewAdapter.EmailViewHolder vh = (EmailRecyclerViewAdapter.EmailViewHolder) holder;
                vh.setMessage((Message) contents.get(position));
                vh.bindData();
                return;
            }
            case FOLDER:{
                FoldersListRecyclerViewAdapter.FolderViewHolder vh = (FoldersListRecyclerViewAdapter.FolderViewHolder) holder;
                vh.setFolder((FolderMetadata) contents.get(position));
                vh.bindData();
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public enum contentTypes {
        FOLDER, EMAIL
    }
}
