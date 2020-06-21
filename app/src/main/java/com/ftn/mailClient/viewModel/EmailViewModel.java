package com.ftn.mailClient.viewModel;


import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ftn.mailClient.model.MessageMetadata;
import com.ftn.mailClient.repository.MessageRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

public class EmailViewModel extends AndroidViewModel {
    private MessageRepository messageRepository;
    private LiveData<MessageMetadata> message;
    private Long messageID;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public EmailViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMessage(Long id){
        messageID = id;
        messageRepository.fetchMessage(id);
        //message = messageRepository.getMetadataMessageById(id);
    }

    public LiveData<MessageMetadata> getFolder(){
        return message;
    }


    public LiveData<FetchStatus> syncMail(){
        return messageRepository.syncMail(messageID);
    }

    public Long getMailId() {
        return messageID;
    }
}
