package com.ftn.mailClient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactSuggestionArrayAdapter extends ArrayAdapter<Contact> {
    List<Contact> contactList;

    public ContactSuggestionArrayAdapter(@NonNull Context context) {
        super(context, R.layout.dropdown_list_item_contact);
        contactList = new ArrayList<>();
    }

    @Override
    public void add(@Nullable Contact object) {
        contactList.add(object);
        super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Contact> collection) {
        contactList.addAll(collection);
        super.addAll(collection);
    }

    @Override
    public void clear() {
        contactList.clear();
        super.clear();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dropdown_list_item_contact, parent, false
            );
        }
        TextView textView = convertView.findViewById(R.id.list_item_text);
        textView.setText(getItem(position).toString());
        return convertView;
    }



    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults= new FilterResults();
                filterResults.values = contactList;
                filterResults.count = contactList.size();
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<Contact>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
