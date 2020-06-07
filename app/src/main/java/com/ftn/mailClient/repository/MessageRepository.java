package com.ftn.mailClient.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Message;

import java.util.List;

public class MessageRepository extends Repository<Message, MessageDao> {

    public MessageRepository(Application application) {
        super(application);
        LocalDatabase database = LocalDatabase.getInstance(application);
        dao = database.messageDao();
    }

    @Override
    public void insert(Message value) {

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
