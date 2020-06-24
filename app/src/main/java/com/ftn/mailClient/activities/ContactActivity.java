package com.ftn.mailClient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.contactsActivity.ContactsActivity;
import com.ftn.mailClient.viewModel.ContactViewModel;

public class ContactActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;

    private TextView textViewName;
    private TextView textViewLast;
    private TextView textViewDisplay;
    private TextView textViewEmail;
    private Menu supportMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        getSupportActionBar().setTitle(R.string.contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        if(getIntent().hasExtra("contactId"))
            contactViewModel.setContactId(getIntent().getLongExtra("contactId", -55));

        textViewName = findViewById(R.id.fist_name_contact);
        textViewLast = findViewById(R.id.last_name_contact);
        textViewDisplay = findViewById(R.id.display_name_contact);
        textViewEmail = findViewById(R.id.email_name_contact);

            contactViewModel.getContact().observe(this, contact -> {
                if(contact != null){
                    textViewName.setText(contact.getFirstName().toString());
                    textViewLast.setText(contact.getLastName().toString());
                    textViewDisplay.setText(contact.getDisplayName().toString());
                    textViewEmail.setText(contact.getEmail().toString());

                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        this.supportMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_contact: {
                contactViewModel.deleteContact();
                break;
            }
            case R.id.save_contact: {
                contactViewModel.saveContact();
                break;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goBack(){
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
