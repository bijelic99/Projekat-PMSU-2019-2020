package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class MessageRepository extends Repository<Message, MessageDao> {

    public MessageRepository(Application application) {
        super(application);
        LocalDatabase database = LocalDatabase.getInstance(application);
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
        return null;
    }

    @Override
    public LiveData<List<Message>> getByIdList(List<Long> ids) {
        return dao.getMessages(ids);
    }
}
