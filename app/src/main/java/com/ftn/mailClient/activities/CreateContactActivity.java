package com.ftn.mailClient.activities;

import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.contactsActivity.ContactsActivity;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.AccountEmailsViewModel;
import com.ftn.mailClient.viewModel.CreateContactViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateContactActivity extends AppCompatActivity {
    private ImageView imageView;
    private static final int PICK_IMAGE = 2;
    private CreateContactViewModel createContactViewModel;
    private LinearLayout contentLinearLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        getSupportActionBar().setTitle(R.string.add_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createContactViewModel = new ViewModelProvider(this).get(CreateContactViewModel.class);

        imageView = findViewById(R.id.image);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_person_outline_white_24dp));
        imageView.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            galleryIntent.setAction("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserInt = Intent.createChooser(galleryIntent, getString(R.string.select_picture));
            chooserInt.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserInt, PICK_IMAGE);

        });

        contentLinearLayout = findViewById(R.id.content_linear_layout);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            createContactViewModel.setImagePath(data.getData());
            try {
                Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), createContactViewModel.getImagePath()));
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e){
                imageView.setImageDrawable(getDrawable(R.drawable.ic_person_outline_white_24dp));
            }
        }
    }

    public void saveButtonClicked(){


        EditText first = findViewById(R.id.editFirstContact);
        EditText last = findViewById(R.id.editLastContact);
        EditText display = findViewById(R.id.editDisplayContact);
        EditText email = findViewById(R.id.editEmailContact);
        EditText format = findViewById(R.id.editFormatContact);



        if(email.getText().toString() != null && email.getText().toString().matches("^.+@.+\\..+$")) {
            contentLinearLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Contact contact = createContactViewModel.getContact();
            contact.setFirstName(first.getText().toString());
            contact.setLastName(last.getText().toString());
            contact.setDisplayName(display.getText().toString());
            contact.setEmail(email.getText().toString());
            contact.setFormat(format.getText().toString());

            LiveData<FetchStatus> fetchStatusLiveData = createContactViewModel.insert();
            fetchStatusLiveData.observe(this, new Observer<FetchStatus>() {
                @Override
                public void onChanged(FetchStatus fetchStatus) {
                    if (fetchStatus.equals(FetchStatus.ERROR))
                        Toast.makeText(getApplicationContext(), R.string.create_error, Toast.LENGTH_SHORT).show();
                    if (!fetchStatus.equals(FetchStatus.FETCHING)) {
                        contentLinearLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        fetchStatusLiveData.removeObserver(this);
                    }
                    if (fetchStatus.equals(FetchStatus.DONE)) redirectToContacts();
                }
            });
        }
        else {
            findViewById(R.id.email_req_t_w).setVisibility(View.VISIBLE);
        }
    }

    private void redirectToContacts(){
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:{
                saveButtonClicked();
                break;
            }
            case android.R.id.home:{
                redirectToContacts();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Disable back button
    /*
    @Override
    public void onBackPressed() { }
    */
}
