package com.ftn.mailClient.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.folderActivity.FolderActivity;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;

import java.util.ArrayList;

public class FoldersListRecyclerViewAdapter extends RecyclerView.Adapter<FoldersListRecyclerViewAdapter.FolderViewHolder> {
    private ArrayList<Folder> folders;
    private Context context;

    public FoldersListRecyclerViewAdapter(Context context, ArrayList<Folder> folders){
        this.folders = folders;
        this.context = context;
    }


    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_folder, parent, false);
        FolderViewHolder holder = new FolderViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        holder.setFolder(folders.get(position));
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder{
        Folder folder;

        TextView name;
        TextView noOfMessages;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public Folder getFolder() {
            return folder;
        }

        public void setFolder(Folder folder) {
            this.folder = folder;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bindData(){
            /*
            if(name == null) name = (TextView)itemView.findViewById(R.id.folder_name);
            if(noOfMessages == null) noOfMessages = (TextView)itemView.findViewById(R.id.folder_no_of_messages);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("aaa", "clckd");
                    Intent intent = new Intent(itemView.getContext(), FolderActivity.class);
                    intent.putExtra("folder", getFolder());
                    itemView.getContext().startActivity(intent);
                }
            });
            name.setText(folder.getName());
            noOfMessages.setText(folder.getFolderContents() != null ? folder.getFolderContents().stream().filter( f -> f.getClass() == Message.class).count()+"" : "0");
            */
        }
    }
}
