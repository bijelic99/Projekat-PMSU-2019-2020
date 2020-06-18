package com.ftn.mailClient.repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.ContactDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.repository.asyncTasks.ContactAsyncTasks;
import com.ftn.mailClient.repository.asyncTasks.PhotoAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class ContactRepository extends Repository<Contact, ContactDao> {

    public ContactRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.contactDao();
    }

    @Override
    public LiveData<FetchStatus> insert(Contact value) {
        return null;
    }

    @Override
    public void update(Contact value) {

    }

    @Override
    public void delete(Contact value) {

    }

    @Override
    public LiveData<List<Contact>> getAll() {
        return dao.getAllContacts();
    }

    @Override
    public LiveData<Contact> getById(Long id) {
        return null;
    }

    @Override
    public LiveData<List<Contact>> getByIdList(List<Long> ids) {
        return null;
    }

    public LiveData<FetchStatus> fetchContacts(Long userId){
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new ContactAsyncTasks.FetchContactsAsyncTask(database, value -> fetchStatusMutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR)).execute(userId);
        return fetchStatusMutableLiveData;
    }

    /**
     * First we upload image to server, then by callback we also add new contact
     * @param contact New contact
     * @param imageBitmap Bitmap of the image you wish to add to contact
     * @return Status of fetching
     */
    public LiveData<FetchStatus> insert(Contact contact, Bitmap imageBitmap, Long userId) {
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        if(imageBitmap != null) new PhotoAsyncTasks.InsertPhotoAsyncTask(value -> {
            if(value != null) {
                contact.setPhoto(value);
                new ContactAsyncTasks.InsertContactAsyncTask(database, value1 -> fetchStatusMutableLiveData.setValue(value1 ? FetchStatus.DONE : FetchStatus.ERROR), userId)
                        .execute(contact);
            }
            else fetchStatusMutableLiveData.setValue(FetchStatus.ERROR);
        }).execute(imageBitmap);
        else insert(contact, userId);

        return fetchStatusMutableLiveData;
    }

    public LiveData<FetchStatus> insert(Contact value, Long userId) {
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new ContactAsyncTasks.InsertContactAsyncTask(database, value1 -> fetchStatusMutableLiveData.setValue(value1 ? FetchStatus.DONE : FetchStatus.ERROR), userId).execute(value);
        return fetchStatusMutableLiveData;
    }

    public LiveData<List<Contact>> getContacts(String term){
        return dao.getContacts(term);
    }
}
