package com.ftn.mailClient.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.adapters.ContactSuggestionArrayAdapter;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.viewModel.CreateContactViewModel;
import com.ftn.mailClient.viewModel.CreateEmailViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class CreateEmailActivity extends AppCompatActivity {
    private CreateEmailViewModel createEmailViewModel;

    private ChipGroup toChipGroup;
    private ChipGroup ccChipGroup;
    private ChipGroup bccChipGroup;
    private ChipGroup attachmentChipGroup;
    private AutoCompleteTextView toAutoCompleteTextView;
    private AutoCompleteTextView ccAutoCompleteTextView;
    private AutoCompleteTextView bccAutoCompleteTextView;
    private EditText subjectEditText;
    private EditText contentEditText;

    private String toInputString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email);

        createEmailViewModel = new ViewModelProvider(this).get(CreateEmailViewModel.class);

        getSupportActionBar().setTitle(R.string.send_mail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toChipGroup = findViewById(R.id.chip_group_to);
        ccChipGroup = findViewById(R.id.chip_group_cc);
        bccChipGroup = findViewById(R.id.chip_group_bcc);
        attachmentChipGroup = findViewById(R.id.chip_group_attachment);

        toAutoCompleteTextView = findViewById(R.id.actw_to);
        ccAutoCompleteTextView = findViewById(R.id.actw_cc);
        bccAutoCompleteTextView = findViewById(R.id.actw_bcc);

        subjectEditText = findViewById(R.id.editText_subject);
        contentEditText = findViewById(R.id.editText_content);

        createEmailViewModel.getToList().observe(this, contacts -> {
            toChipGroup.removeAllViews();
            if(contacts != null) {
                int i = 0;
                for (Contact contact : contacts) {
                    View view = LayoutInflater.from(this).inflate(R.layout.contact_chip, toChipGroup, false);
                    Chip chip = view.findViewById(R.id.contact_chip);
                    chip.setText(contact.toString());
                    chip.setCheckable(false);
                    chip.setCloseIconVisible(true);
                    Integer index = i;
                    chip.setOnCloseIconClickListener(v -> createEmailViewModel.removeFromToList(index));
                    toChipGroup.addView(chip, i++);
                }
            }
        });

        ContactSuggestionArrayAdapter adapter = new ContactSuggestionArrayAdapter(this);

        toInputString = "";

        createEmailViewModel.toListFiltered.observe(this, contacts -> {
            adapter.clear();
            if(!toInputString.isEmpty() && toInputString.matches("^.+@.+\\..+$")) {
                Contact c = new Contact();
                c.setEmail(toInputString);
                adapter.add(c);
            }
            if(contacts != null);
            adapter.addAll(contacts);
            adapter.notifyDataSetChanged();
            Log.i("contacts", contacts.toString());
        });
        toAutoCompleteTextView.setAdapter(adapter);
        toAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                toInputString = s.toString();
                createEmailViewModel.setToSearchTerm(s.toString());
            }
        });

        toAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Contact contact = adapter.getItem(position);
            createEmailViewModel.addToToList(contact);
            toAutoCompleteTextView.setText("");
            System.out.println(position+" Selected");
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_email_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.send_item:{
                break;
            }
            case R.id.add_attachment_item:{
                break;
            }
            case R.id.cancel_item:{
                break;
            }
            case android.R.id.home:{
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
