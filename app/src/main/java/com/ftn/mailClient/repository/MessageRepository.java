package com.ftn.mailClient.repository;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.asyncTasks.MessageAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.ArrayList;
import java.util.List;

public class MessageRepository extends Repository<Message, MessageDao> {


    public MessageRepository(Application application) {
        super(application);
        database = LocalDatabase.getInstance(application);
        dao = database.messageDao();
    }

    @Override
    public LiveData<FetchStatus> insert(Tag value, Long userID) {
        return null;
    }

    @Override
    public void update(Message value) {

    }

    @Override
    public void delete(Message value) {

    }

    @Override
    public LiveData<List<Message>> getAll() {
        return null;
    }

    @Override
    public LiveData<Message> getById(Long id) {
        return dao.getMessageById(id);
    }

    @Override
    public LiveData<List<Message>> getByIdList(List<Long> ids) {
        return dao.getMessages(ids);
    }

    public LiveData<FetchStatus> sendMessage(List<Contact> toList, List<Contact> ccList, List<Contact> bccList, String subject, String contents, List<Uri> attachments, Long accountId){
        MutableLiveData<FetchStatus> fetchStatusLiveData = new MutableLiveData<>(FetchStatus.FETCHING);

        Bundle bundle = new Bundle();
        bundle.putSerializable("toList", new ArrayList<>(toList));
        bundle.putSerializable("ccList", new ArrayList<>(ccList));
        bundle.putSerializable("bccList", new ArrayList<>(bccList));
        bundle.putLong("accountId", accountId);
        bundle.putString("subject", subject);
        bundle.putString("contents", contents);
        bundle.putSerializable("attachments", new ArrayList<>(attachments));


        new MessageAsyncTasks.SendMessageAsyncTask(database,
                value -> fetchStatusLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR),
                accountId, application.getContentResolver())
                .execute(bundle);

        return fetchStatusLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<FetchStatus> addToDrafts(List<Contact> toList, List<Contact> ccList, List<Contact> bccList, String subject, String contents, List<Uri> attachments, Long accountId){
        MutableLiveData<FetchStatus> fetchStatusLiveData = new MutableLiveData<>(FetchStatus.FETCHING);

        Bundle bundle = new Bundle();
        bundle.putSerializable("toList", new ArrayList<>(toList));
        bundle.putSerializable("ccList", new ArrayList<>(ccList));
        bundle.putSerializable("bccList", new ArrayList<>(bccList));
        bundle.putLong("accountId", accountId);
        bundle.putString("subject", subject);
        bundle.putString("contents", contents);
        bundle.putSerializable("attachments", new ArrayList<>(attachments));

        new MessageAsyncTasks.AddToDraftsAsyncTask(database,
                value -> fetchStatusLiveData.setValue(value ? FetchStatus.DONE : FetchStatus.ERROR),
                accountId, application.getContentResolver()
                ).execute(bundle);

        return fetchStatusLiveData;
    }
}
