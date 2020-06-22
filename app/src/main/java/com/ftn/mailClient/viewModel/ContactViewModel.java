package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.ContactMetadata;
import com.ftn.mailClient.repository.ContactRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository contactRepository;
    private LiveData<Contact> contact;
    private Long contactId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setContact(Long id){
        contactId = id;
        contactRepository.fetchContact(id);
        contact = contactRepository.getMetadataContactById(id);
    }

    public LiveData<Contact> getContact(){
        return contact;
    }

    public LiveData<FetchStatus> syncContact(){
        return contactRepository.syncContact(contactId);
    }

    public Long getContactId() {
        return contactId;
    }
}
