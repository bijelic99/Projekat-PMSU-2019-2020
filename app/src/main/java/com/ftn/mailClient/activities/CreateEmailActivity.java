package com.ftn.mailClient.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.adapters.ContactSuggestionArrayAdapter;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.utill.ContactChipClickEvent;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.CreateEmailViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

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
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    private String toInputString;
    private String ccInputString;
    private String bccInputString;

    private static final int ATTACHMENT_SELECTED_CODE = 112;

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

        linearLayout = findViewById(R.id.linear_layout);
        progressBar = findViewById(R.id.progress_bar);

        subjectEditText = findViewById(R.id.editText_subject);
        contentEditText = findViewById(R.id.editText_content);

        ContactSuggestionArrayAdapter toAdapter = new ContactSuggestionArrayAdapter(this);

        createEmailViewModel.getToList().observe(this, contacts -> {
            toChipGroup.removeAllViews();
            if (contacts != null) {
                setContactChipsToChipGroup(this, toChipGroup, contacts, index -> createEmailViewModel.removeFromToList(index));
                toAdapter.setSelectedContacts(contacts);
            }
        });


        toInputString = "";

        createEmailViewModel.toListFiltered.observe(this, contacts -> {
            if (contacts != null)
                setContactSuggestionAdapterResults(toAdapter, toInputString, contacts);
        });
        toAutoCompleteTextView.setAdapter(toAdapter);
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
            Contact contact = toAdapter.getItem(position);
            createEmailViewModel.addToToList(contact);
            toAutoCompleteTextView.setText("");
        });

        ContactSuggestionArrayAdapter ccAdapter = new ContactSuggestionArrayAdapter(this);

        createEmailViewModel.getCcList().observe(this, contacts -> {
            ccChipGroup.removeAllViews();
            if (contacts != null) {
                setContactChipsToChipGroup(this, ccChipGroup, contacts, index -> createEmailViewModel.removeFromCCList(index));
                ccAdapter.setSelectedContacts(contacts);
            }
        });

        ccInputString = "";

        createEmailViewModel.ccListFiltered.observe(this, contacts -> {
            if (contacts != null)
                setContactSuggestionAdapterResults(ccAdapter, ccInputString, contacts);
        });
        ccAutoCompleteTextView.setAdapter(ccAdapter);
        ccAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ccInputString = s.toString();
                createEmailViewModel.setCcSearchTerm(s.toString());
            }
        });

        ccAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Contact contact = ccAdapter.getItem(position);
            createEmailViewModel.addToCCList(contact);
            ccAutoCompleteTextView.setText("");
        });

        ContactSuggestionArrayAdapter bccAdapter = new ContactSuggestionArrayAdapter(this);

        createEmailViewModel.getBccList().observe(this, contacts -> {
            if (contacts != null) {
                setContactChipsToChipGroup(this, bccChipGroup, contacts, index -> createEmailViewModel.removeFromBccList(index));
                bccAdapter.setSelectedContacts(contacts);
            }
        });

        bccInputString = "";

        createEmailViewModel.bccListFiltered.observe(this, contacts -> {
            if (contacts != null)
                setContactSuggestionAdapterResults(bccAdapter, bccInputString, contacts);
        });
        bccAutoCompleteTextView.setAdapter(bccAdapter);
        bccAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bccInputString = s.toString();
                createEmailViewModel.setBccSearchTerm(s.toString());
            }
        });

        bccAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Contact contact = bccAdapter.getItem(position);
            createEmailViewModel.addToBccList(contact);
            bccAutoCompleteTextView.setText("");
        });

        subjectEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                createEmailViewModel.setSubject(s.toString());
            }
        });

        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                createEmailViewModel.setContent(s.toString());
            }
        });

        createEmailViewModel.getAttachmentUriList().observe(this, uris -> {
            if (uris != null) {
                attachmentChipGroup.removeAllViews();
                int i = 0;
                for (Uri uri : uris) {
                    String filename = "";
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    }finally {
                        if(cursor != null) cursor.close();
                    }
                    View view = LayoutInflater.from(this).inflate(R.layout.chip_attachment, attachmentChipGroup, false);
                    Chip chip = view.findViewById(R.id.attachment_chip);
                    chip.setText(filename);
                    Integer index = i;
                    chip.setOnCloseIconClickListener(v -> createEmailViewModel.removeFromAttachmentList(index));
                    attachmentChipGroup.addView(chip, i++);
                }

            }
        });
    }

    private static void setContactSuggestionAdapterResults(ContactSuggestionArrayAdapter adapter, String searchString, List<Contact> contacts) {
        adapter.clear();
        if (!searchString.isEmpty() && searchString.matches("^.+@.+\\..+$")) {
            Contact c = new Contact();
            c.setEmail(searchString);
            adapter.add(c);
        }
        if (contacts != null) adapter.addAll(contacts);
        adapter.notifyDataSetChanged();
    }

    private static void setContactChipsToChipGroup(Context context, ChipGroup chipGroup, List<Contact> contacts, ContactChipClickEvent contactChipClickEvent) {
        chipGroup.removeAllViews();
        if (contacts != null) {
            int i = 0;
            for (Contact contact : contacts) {
                View view = LayoutInflater.from(context).inflate(R.layout.chip_contact, chipGroup, false);
                Chip chip = view.findViewById(R.id.contact_chip);
                chip.setText(contact.toString());
                chip.setCheckable(false);
                chip.setCloseIconVisible(true);
                Integer index = i;
                chip.setOnCloseIconClickListener(v -> contactChipClickEvent.clickFunction(index));
                chipGroup.addView(chip, i++);
            }
        }
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
        switch (item.getItemId()) {
            case R.id.send_item: {
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                createEmailViewModel.sendMessage().observe(this, fetchStatus -> {
                    if(fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(this, R.string.create_error, Toast.LENGTH_SHORT).show();
                    if(fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)){
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                });
                break;
            }
            case R.id.add_attachment_item: {
                Intent chooseAttachmentsIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                chooseAttachmentsIntent.setType("*/*");
                chooseAttachmentsIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                Intent intent = Intent.createChooser(chooseAttachmentsIntent, "Choose attachments to add");
                startActivityForResult(intent, ATTACHMENT_SELECTED_CODE);
                break;
            }
            case R.id.cancel_item: {
                Intent intent = new Intent(getBaseContext(), EmailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
            case android.R.id.home: {
                saveToDraftsAndRedirect();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ATTACHMENT_SELECTED_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        createEmailViewModel.addToAttachmentList(uri);
                    }
                } else {
                    Uri uri = data.getData();
                    createEmailViewModel.addToAttachmentList(uri);
                }
            }
        }
    }

    //Disable back button

    @Override
    public void onBackPressed() {
        saveToDraftsAndRedirect();

    }

    private void saveToDraftsAndRedirect(){
        createEmailViewModel.saveToDrafts();
        Intent intent = new Intent(getBaseContext(), EmailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
