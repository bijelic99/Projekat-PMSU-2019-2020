package com.ftn.mailClient.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.contactsActivity.ContactsActivity;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.CreateContactViewModel;
import com.ftn.mailClient.viewModel.CreateTagViewModel;


public class CreateTagActivity extends AppCompatActivity {


    private CreateTagViewModel createTagViewModel;
    private LinearLayout contentLinearLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tag_activity);
        getSupportActionBar().setTitle(R.string.add_tag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createTagViewModel = new ViewModelProvider(this).get(CreateTagViewModel.class);


        contentLinearLayout = findViewById(R.id.linear_layout_tag);
        progressBar = findViewById(R.id.progress_bar_tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveButtonClicked(){


        EditText name = findViewById(R.id.editTag);




        if(name.getText().toString() != null) {
            contentLinearLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Tag tag = createTagViewModel.getTag();
            tag.setName(name.getText().toString());


            LiveData<FetchStatus> fetchStatusLiveData = createTagViewModel.insert();
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
                    if (fetchStatus.equals(FetchStatus.DONE)) redirectToTags();
                }
            });
        }
    }

    private void redirectToTags(){
        Intent intent = new Intent(this, TagsActivity.class);
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
                redirectToTags();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
