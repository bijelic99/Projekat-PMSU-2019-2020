package com.ftn.mailClient.adapters;

import android.content.Context;
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

import java.util.ArrayList;

public class EmailRecyclerViewAdapter extends RecyclerView.Adapter<EmailRecyclerViewAdapter.EmailViewHolder> {

    Message[] messages;
    Context context;

    public EmailRecyclerViewAdapter(Context context, Message[] messages){
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
        holder.setMessage(messages[position]);
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

    public static class EmailViewHolder extends ViewHolder{
        Message message;

        TextView fullName;
        TextView subject;
        TextView tags;
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
                tags = (TextView)itemView.findViewById(R.id.textViewTags);
            }
            if(content == null){
                content = (TextView)itemView.findViewById(R.id.textViewContent);
            }

            fullName.setText(itemView.getResources().getString(R.string.users_fullname, getMessage().getFrom().getFirstName(), getMessage().getFrom().getLastName()));
            subject.setText(message.getSubject());
            //TODO napraviti isto listview za tagove
            tags.setText(itemView.getResources().getString(R.string.tags_placeholder));
            content.setText(message.getContent());
        }
    }
}
