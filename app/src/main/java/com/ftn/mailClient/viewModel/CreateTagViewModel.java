package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.repository.TagRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

public class CreateTagViewModel extends AndroidViewModel {
    private Long userID;
    private Tag tag;
    private TagRepository tagRepository;

    public CreateTagViewModel(@Nullable Application application){
        super(application);
        tagRepository = new TagRepository(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(application.getString(R.string.user_id))){
            userID = sharedPreferences.getLong(application.getString(R.string.user_id), -55L);
            tag = new Tag();
        }
        else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
    }
    public Tag getTag(){
        return tag;
    }

    public LiveData<FetchStatus> insert(){
        if(userID != null) return tagRepository.insert(tag, userID);
        else return null;
    }

}
