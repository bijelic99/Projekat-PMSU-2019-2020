package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.repository.ContactRepository;

public class ContactViewModel extends AndroidViewModel {
    private Long contactId;
    private LiveData<Contact> contact;
    private ContactRepository contactRepository;
    private Long accountId;

    public ContactViewModel(@NonNull Application application){
        super(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(application.getString(R.string.user_account_id))) {
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        } else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
        contactRepository = new ContactRepository(application);
    }

    public void setContactId(Long id){
        this.contactId = id;
    }

    public LiveData<Contact> getContact() {
        if(contact == null) contact = contactRepository.getById(contactId);
        return contact;
    }

    public void saveContact(){
    }

    public void deleteContact(){
    }
}
