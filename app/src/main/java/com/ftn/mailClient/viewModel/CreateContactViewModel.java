package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.repository.ContactRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.io.IOException;

public class CreateContactViewModel extends AndroidViewModel {
    private Long userId;
    private Contact contact;
    private Uri imagePath;
    private ContactRepository contactRepository;
    private Bitmap imageBitmap;


    public CreateContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(application.getString(R.string.user_id))){
            userId = sharedPreferences.getLong(application.getString(R.string.user_id), -55L);
            contact = new Contact();
        }
        else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
    }

    public Contact getContact() {
        return contact;
    }

    public void setImagePath(Uri imagePath) {
        this.imagePath = imagePath;
        try {
            imageBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getApplication().getContentResolver(), imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            imageBitmap = null;
        }
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public LiveData<FetchStatus> insert(){
        if(userId != null) return contactRepository.insert(contact, imageBitmap, userId);
        else return null;
    }

}
