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
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.model.MessageMetadata;
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

    public LiveData<FetchStatus> syncMail(Long messageId){
        MutableLiveData<FetchStatus> fetchStatus = new MutableLiveData<>(FetchStatus.FETCHING);
        new MessageAsyncTasks.MessageSyncAsyncTask(database, value -> {
            if(value) fetchStatus.setValue(FetchStatus.DONE);
            else fetchStatus.setValue(FetchStatus.ERROR);
        }).execute(messageId);
        return fetchStatus;
    }

    @Override
    public void update(Message value) {

    }

    @Override
    public void delete(Message value) {

    }

//    public LiveData<MessageMetadata> getMetadataMessageById(Long id){
//        return dao.getMessageMetadataById(id);
//    }

    @Override
    public LiveData<List<Message>> getAll() {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public LiveData<Message> getById(Long id) {
        LiveData<Message> messageLiveData = dao.getLiveDataMessageById(id);
        if(messageLiveData.getValue() == null) fetchMessage(id);

        return messageLiveData;
    }

    public void fetchMessage(Long messageId){
        new MessageAsyncTasks.MessageFetchAsyncTask(database).execute(messageId);
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
