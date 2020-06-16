package com.ftn.mailClient.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.utill.LoadImageAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {
    private List<Contact> contacts;
    private Context context;

    public ContactRecyclerViewAdapter(Context context, List<Contact> contacts){
        this.context = context;
        this.contacts = contacts;
    }

    public ContactRecyclerViewAdapter(Context context){
        this.context = context;
        this.contacts = new ArrayList<>();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact, parent, false);
        ContactViewHolder holder = new ContactViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(contacts.get(position));
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        Contact contact;

        TextView displayName;
        TextView email;
        ImageView profilePicture;

        public ContactViewHolder(View itemView){
            super(itemView);
        }

        public Contact getContact() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        public void bindData(){
            if(displayName == null){
                displayName = (TextView)itemView.findViewById(R.id.textViewDisplayName);
            }
            if(email == null){
                email = (TextView)itemView.findViewById(R.id.textViewEmail);
            }
            if(profilePicture == null){
                profilePicture = (ImageView)itemView.findViewById(R.id.imageViewProfilePicture);
            }

            displayName.setText(getContact().getDisplayName());
            email.setText(getContact().getEmail());
            if(getContact().getPhoto() != null && getContact().getPhoto().getPath() != null){
                new LoadImageAsyncTask(value -> {
                    if(value != null)
                        profilePicture.setImageBitmap(value);
                }).execute(getContact().getPhoto().getPath());

            }
            else {
                Drawable personIcon = itemView.getContext().getDrawable(R.drawable.ic_person_outline_white_24dp);
                profilePicture.setImageDrawable(personIcon);
            }

        }

    }
}
