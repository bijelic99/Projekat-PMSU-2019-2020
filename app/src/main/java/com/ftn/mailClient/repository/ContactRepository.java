package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.ContactDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.repository.asyncTasks.ContactAsyncTasks;
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

    public LiveData<FetchStatus> fetchContacts(Long accountId){
        MutableLiveData<FetchStatus> fetchStatusMutableLiveData = new MutableLiveData<>(FetchStatus.FETCHING);
        new ContactAsyncTasks.FetchContactsAsyncTask(database, value -> fetchStatusMutableLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR)).execute(accountId);
        return fetchStatusMutableLiveData;
    }
}
