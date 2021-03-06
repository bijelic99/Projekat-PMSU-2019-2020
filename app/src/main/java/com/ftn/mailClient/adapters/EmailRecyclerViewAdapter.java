package com.ftn.mailClient.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.EmailActivity;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.utill.FilterEmail;
import com.ftn.mailClient.utill.FolderContentsComparatorInterface;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

public class EmailRecyclerViewAdapter extends RecyclerView.Adapter<EmailRecyclerViewAdapter.EmailViewHolder> {

    private List<Message> unfilteredMessages;
    private List<Message> messages;
    private Context context;
    private FilterEmail filterEmail;

    public EmailRecyclerViewAdapter(Context context, ArrayList<Message> messages){
        this.messages = messages;
        this.context = context;
    }

    public EmailRecyclerViewAdapter(Context context){
        this.unfilteredMessages = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMessages(List<Message> messages) {
        this.unfilteredMessages = messages;
        if(filterEmail != null)
            this.messages = unfilteredMessages.stream().filter(message -> getMessageMetadata(message).toLowerCase().contains(filterEmail.getSearchCriteria())).collect(Collectors.toList());
        else this.messages = unfilteredMessages;
        Collections.sort( this.messages, FolderContentsComparatorInterface::folderContentsComparator);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getMessageMetadata(Message message) {
        StringJoiner sj = new StringJoiner(" ");
        if(message.getSubject() != null)
            sj.add(message.getSubject());
        if(message.getContent() != null)
            sj.add(message.getContent());
        if(message.getFrom() != null)
            sj.add(message.getFrom().getEmail());
        if (!message.getBcc().isEmpty())
            message.getBcc().stream().forEach(contact -> sj.add(contact.getEmail()));
        if(!message.getCc().isEmpty())
            message.getCc().stream().forEach(contact -> sj.add(contact.getEmail()));
        message.getTo().stream().forEach(contact -> sj.add(contact.getEmail()));
        message.getTags().stream().forEach(tag -> sj.add(tag.getName()));

        return sj.toString();
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

    public void setFilterEmail(FilterEmail filterEmail) {
        this.filterEmail = filterEmail;
        if(filterEmail != null)
            messages = unfilteredMessages.stream().filter(message -> getMessageMetadata(message).contains(filterEmail.getSearchCriteria())).collect(Collectors.toList());
        else messages = unfilteredMessages;
        notifyDataSetChanged();
    }

    public static class EmailViewHolder extends ViewHolder{
        Message message;

        TextView fullName;
        TextView subject;
        ChipGroup tags;
        TextView content;
        TextView timeRecived;

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
            if(timeRecived == null) timeRecived = (TextView)itemView.findViewById(R.id.textViewDateTime);
            //Potrebno kako bi ispisivao pravilno od koga je mail...
            String name = "";
            if(getMessage().getFrom() != null)
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
            timeRecived.setText(message.getDateTime() != null ? message.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME) : itemView.getResources().getText(R.string.no_receive_time));
            itemView.setClickable(true);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), EmailActivity.class);
                intent.putExtra("mailId", getMessage().getId());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
