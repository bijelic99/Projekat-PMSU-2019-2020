package com.ftn.mailClient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.utill.FolderContentsComparatorInterface;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagRecyclerViewAdapter extends RecyclerView.Adapter<TagRecyclerViewAdapter.TagViewHolder> {

    private List<Tag> tags;
    private Context context;

    public TagRecyclerViewAdapter(Context context, ArrayList<Tag> tags){
        this.tags = tags;
        this.context = context;
    }

    public TagRecyclerViewAdapter(Context context){
        this.tags = new ArrayList<>();
        this.context = context;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
        Collections.sort( this.tags, FolderContentsComparatorInterface::folderContentsComparator);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagRecyclerViewAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_email_tag, parent, false);
        TagRecyclerViewAdapter.TagViewHolder holder = new TagRecyclerViewAdapter.TagViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagRecyclerViewAdapter.TagViewHolder holder, int position) {
        holder.setTag(tags.get(position));
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        Tag tag;

        Chip name;



        public Tag getTag() {
            return tag;
        }

        public void setTag(Tag tag) {
            this.tag = tag;
        }

        public TagViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData() {
            if (name == null) {
                name = (Chip) itemView.findViewById(R.id.chipTagUsers);
            }
            name.setText(tag.getName());
        }
    }
}
