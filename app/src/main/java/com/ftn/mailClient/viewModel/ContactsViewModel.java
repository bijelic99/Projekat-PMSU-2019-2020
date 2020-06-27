package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.repository.ContactRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {
    private Long userId;
    private LiveData<List<Contact>> contacts;
    private ContactRepository contactRepository;


    public ContactsViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(application.getString(R.string.user_id))){
            userId = sharedPreferences.getLong(application.getString(R.string.user_id), -55L);
        }
        else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
    }

    public LiveData<List<Contact>> getContacts() {
        if (contacts == null) contacts = contactRepository.getAll();
        return contacts;
    }

    public LiveData<FetchStatus> fetchContacts(){
        if(userId != null) return contactRepository.fetchContacts(userId);
        return new MutableLiveData<>(FetchStatus.DONE);
    }
}
