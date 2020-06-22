package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.repository.MessageRepository;

public class EmailViewModel extends AndroidViewModel {
    private Long mailId;
    private LiveData<Message> message;
    private MessageRepository messageRepository;
    private Long accountId;

    public EmailViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(application.getString(R.string.user_account_id))) {
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        } else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
        messageRepository = new MessageRepository(application);
    }

    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }

    public LiveData<Message> getMessage() {
        if(message == null) message = messageRepository.getById(mailId);
        return message;
    }

    public void reply() {
    }

    public void replyAll() {
    }

    public void forward() {
    }

    public void saveAttachments() {
    }

    public void deleteMessage() {
    }

    public void editAsDraft() {
    }

    public void addTag() {
    }
}
