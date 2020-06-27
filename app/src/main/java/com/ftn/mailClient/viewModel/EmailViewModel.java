package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.CreateEmailActivity;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.repository.MessageRepository;
import com.ftn.mailClient.repository.asyncTasks.MessageAsyncTasks;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.utill.enums.MessageResponseType;

public class EmailViewModel extends AndroidViewModel {
    private Long mailId;
    private LiveData<Message> message;
    private MessageRepository messageRepository;
    private Long accountId;

    public Long getMailId() {
        return mailId;
    }

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
        Intent intent = new Intent(getApplication(), CreateEmailActivity.class);
        intent.putExtra("mailId", mailId);
        intent.putExtra("mailMode", MessageResponseType.REPLY.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void replyAll() {
        Intent intent = new Intent(getApplication(), CreateEmailActivity.class);
        intent.putExtra("mailId", mailId);
        intent.putExtra("mailMode", MessageResponseType.REPLY_ALL.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void forward() {
        Intent intent = new Intent(getApplication(), CreateEmailActivity.class);
        intent.putExtra("mailId", mailId);
        intent.putExtra("mailMode", MessageResponseType.FORWARD.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void saveAttachments() {
        LiveData<Integer> liveData =messageRepository.saveAttachments(mailId, getApplication());
        liveData.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer != null){
                    Toast.makeText(getApplication(), getApplication().getString(R.string.succesfully_saved_attachments, integer.toString()), Toast.LENGTH_SHORT).show();
                    liveData.removeObserver(this);
                }
            }
        });
    }

    public void editAsDraft() {
        Intent intent = new Intent(getApplication(), CreateEmailActivity.class);
        intent.putExtra("mailId", mailId);
        intent.putExtra("mailMode", MessageResponseType.DRAFT.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void addTag() {
    }

    public LiveData<FetchStatus> deleteMessage(){
        return messageRepository.deleteById(mailId);
    }
}
