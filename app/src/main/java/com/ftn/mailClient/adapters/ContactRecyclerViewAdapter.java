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

import java.util.ArrayList;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {
    private ArrayList<Contact> contacts;
    private Context context;

    public ContactRecyclerViewAdapter(Context context, ArrayList<Contact> contacts){
        this.context = context;
        this.contacts = contacts;
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
            if(getContact().getPhoto() != null){
                //TODO potencijalan problem, ne mogu sad da testiram
                /*
                byte[] byteImg = Base64.decode(getContact().getPhoto().getBase64Photo(), Base64.DEFAULT);
                Drawable image = new BitmapDrawable(itemView.getResources(), BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length));
                profilePicture.setImageDrawable(image);*/

            }
            else {
                profilePicture.setImageResource(R.drawable.ic_person_outline_white_24dp);
            }

        }

    }
}
