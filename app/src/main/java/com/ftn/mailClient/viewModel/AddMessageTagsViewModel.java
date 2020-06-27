package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.authorization.UserAccountInfo;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.MessageRepository;
import com.ftn.mailClient.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddMessageTagsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Tag>> tags;
    private Long messageId;
    private MessageRepository messageRepository;
    private TagRepository tagRepository;
    private LiveData<List<Tag>> allTags;

    public AddMessageTagsViewModel(@NonNull Application application) {
        super(application);
        tags = new MutableLiveData<>(new ArrayList<>());
        messageRepository = new MessageRepository(getApplication());
        tagRepository.fetchTags(UserAccountInfo.getUserAccountInfo().getUserId());
        allTags = tagRepository.getAll();
    }

    public MutableLiveData<List<Tag>> getTags() {
        return tags;
    }

    public LiveData<List<Tag>> getAllTags() {
        return allTags;
    }

    public void addTag(Tag tag){
        List<Tag> tagList = tags.getValue();
        tagList.add(tag);
        tags.setValue(tagList);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeTag(Long tagId){
        List<Tag> tagList = tags.getValue();
        tagList = tagList.stream().filter(tag -> tag.getId() != tagId).collect(Collectors.toList());
        tags.setValue(tagList);
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
        LiveData<Message> m = messageRepository.getById(messageId);
        m.observeForever(new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                if(message != null){
                    tags.setValue(message.getTags() != null ? message.getTags() : new ArrayList<>());
                    m.removeObserver(this);
                }
            }
        });
    }

    public void setTags(){

    }
}
