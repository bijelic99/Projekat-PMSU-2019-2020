package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.AccountRepository;
import com.ftn.mailClient.repository.ContactRepository;
import com.ftn.mailClient.repository.TagRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.ArrayList;
import java.util.List;

public class AccountTagsViewModel extends AndroidViewModel {
    private Long userId;
    private LiveData<List<Tag>> tags;
    private TagRepository tagRepository;


    public AccountTagsViewModel(@NonNull Application application) {
        super(application);
        tagRepository = new TagRepository(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(application.getString(R.string.user_id))){
            userId = sharedPreferences.getLong(application.getString(R.string.user_id), -55L);
        }
        else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
    }

    public LiveData<List<Tag>> getTags() {
        if (tags == null) tags = tagRepository.getAll();
        return tags;
    }

    public LiveData<FetchStatus> fetchTags(){
        if(userId != null) return tagRepository.fetchTags(userId);
        return new MutableLiveData<>(FetchStatus.DONE);
    }
}
