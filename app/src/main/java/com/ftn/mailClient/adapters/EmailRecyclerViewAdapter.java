package com.ftn.mailClient.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.stream.*;

public class EmailRecyclerViewAdapter extends RecyclerView.Adapter<EmailRecyclerViewAdapter.EmailViewHolder> {

    ArrayList<Message> messages;
    Context context;

    public EmailRecyclerViewAdapter(Context context, ArrayList<Message> messages){
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_email, parent, false);
        EmailViewHolder holder = new EmailViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        holder.setMessage(messages.get(position));
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class EmailViewHolder extends ViewHolder{
        Message message;

        TextView fullName;
        TextView subject;
        ChipGroup tags;
        TextView content;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public EmailViewHolder(View itemView){
            super(itemView);
        }

        public void bindData(){
            if(fullName == null){
                fullName = (TextView)itemView.findViewById(R.id.textViewFullName);
            }
            if(subject == null){
                subject = (TextView)itemView.findViewById(R.id.textViewSubject);
            }
            if(tags == null){
                tags = (ChipGroup)itemView.findViewById(R.id.chipGroupEmailTag);
            }
            if(content == null){
                content = (TextView)itemView.findViewById(R.id.textViewContent);
            }
            //Potrebno kako bi ispisivao pravilno od koga je mail...
            String name = "";
            if(getMessage().getFrom().getDisplayName() != null) name = getMessage().getFrom().getDisplayName();
            else if(getMessage().getFrom().getFirstName() != null && getMessage().getFrom().getLastName() != null) name = itemView.getResources().getString(R.string.users_fullname, getMessage().getFrom().getFirstName(), getMessage().getFrom().getLastName());
            else if(getMessage().getFrom().getFirstName() == null || getMessage().getFrom().getLastName() == null) name = getMessage().getFrom().getFirstName() != null ? getMessage().getFrom().getFirstName() : getMessage().getFrom().getLastName() != null ? getMessage().getFrom().getLastName() : getMessage().getFrom().getEmail();

            fullName.setText(name);
            subject.setText(message.getSubject());
            tags.removeAllViews();
            LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
            getMessage().getTags().stream().map( t -> {
                Chip chip = (Chip) layoutInflater.inflate(R.layout.fragment_email_tag, null, false);
                //chip.setBackgroundColor(R.color.white);
                //chip.setTextColor(R.color.black);
                chip.setText(t.getName());
                return chip;
            }).forEach(c -> tags.addView(c));
            content.setText(message.getContent());
        }
    }
}
