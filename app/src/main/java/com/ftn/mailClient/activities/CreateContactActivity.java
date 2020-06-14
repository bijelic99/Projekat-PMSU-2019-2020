package com.ftn.mailClient.activities;

import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.viewModel.AccountEmailsViewModel;
import com.ftn.mailClient.viewModel.CreateContactViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateContactActivity extends AppCompatActivity {
    private ImageView imageView;
    private static final int PICK_IMAGE = 2;
    private CreateContactViewModel createContactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        createContactViewModel = new ViewModelProvider(this).get(CreateContactViewModel.class);

        Button b = findViewById(R.id.buttonContactSave);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClicked();
            }
        });

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

    public void clickHandler(View v) {
        Intent intent = new Intent(this, EmailsActivity.class);
        startActivity(intent);
    }

    public void saveButtonClicked(){
        EditText first = findViewById(R.id.editFirstContact);
        EditText last = findViewById(R.id.editLastContact);
        EditText display = findViewById(R.id.editDisplayContact);
        EditText email = findViewById(R.id.editEmailContact);
        EditText format = findViewById(R.id.editFormatContact);

        Contact contact = createContactViewModel.getContact();
        contact.setFirstName(first.getText().toString());
        contact.setLastName(last.getText().toString());
        contact.setDisplayName(display.getText().toString());
        contact.setEmail(email.getText().toString());
        contact.setFormat(format.getText().toString());

        createContactViewModel.insert();
    }

    //Disable back button
    /*
    @Override
    public void onBackPressed() { }
    */
}
