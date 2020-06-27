package com.ftn.mailClient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.model.Attachment;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.viewModel.CreateContactViewModel;
import com.ftn.mailClient.viewModel.EmailViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class EmailActivity extends AppCompatActivity {
    private EmailViewModel emailViewModel;

    private TextView textViewFrom;
    private TextView textViewTo;
    private TextView textViewCc;
    private TextView textViewSubject;
    private TextView textViewContent;
    private TextView textViewDateTime;
    private ChipGroup chipGroupTags;
    private ChipGroup chipGroupAttachments;
    private Menu supportMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);
        getSupportActionBar().setTitle(R.string.email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailViewModel = new ViewModelProvider(this).get(EmailViewModel.class);

        if(getIntent().hasExtra("mailId"))
            emailViewModel.setMailId(getIntent().getLongExtra("mailId", -55));

        textViewFrom = findViewById(R.id.from);
        textViewCc = findViewById(R.id.cc);
        textViewTo = findViewById(R.id.to);
        textViewSubject = findViewById(R.id.subject);
        textViewContent = findViewById(R.id.content);
        textViewDateTime = findViewById(R.id.date_time);
        chipGroupTags = findViewById(R.id.tag_cg);
        chipGroupAttachments = findViewById(R.id.attachment_cg);


            emailViewModel.getMessage().observe(this, message -> {
                if(message != null) {
                    textViewFrom.setText(message.getFrom().toString());
                    textViewCc.setText(message.getCc().stream().map(Contact::toString).collect(Collectors.joining(",")));
                    textViewTo.setText(message.getTo().stream().map(Contact::toString).collect(Collectors.joining(",")));
                    textViewSubject.setText(message.getSubject());
                    textViewContent.setText(message.getContent());
                    textViewDateTime.setText(message.getDateTime() != null ? message.getDateTime().format(DateTimeFormatter.ISO_DATE_TIME) : "");
                    chipGroupTags.removeAllViews();
                    int i = 0;
                    for(Tag tag : message.getTags()){
                        View view = LayoutInflater.from(this).inflate(R.layout.chip_tag, chipGroupTags, false);
                        Chip chip = view.findViewById(R.id.tag_chip);
                        chip.setText(tag.getName());
                        int[] colors = getResources().getIntArray(R.array.tagColorArray);
                        chip.setBackgroundColor(colors[i++%3]);
                        chipGroupTags.addView(chip);
                    }

                    chipGroupAttachments.removeAllViews();
                    for(Attachment attachment : message.getAttachments()){
                        View view = LayoutInflater.from(this).inflate(R.layout.chip_tag, chipGroupTags, false);
                        Chip chip = view.findViewById(R.id.tag_chip);
                        chip.setText(attachment.getName());
                        chip.setClickable(false);
                        chip.setCloseIconVisible(false);
                        chipGroupAttachments.addView(chip);

                    }
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.email_menu, menu);
        this.supportMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.reply: {
                emailViewModel.reply();
                break;
            }
            case R.id.reply_all: {
                emailViewModel.replyAll();
                break;
            }
            case R.id.forward:{
                emailViewModel.forward();
                break;
            }
            case R.id.save_attachments:{
                emailViewModel.saveAttachments();
                break;
            }
            case R.id.delete:{
                LiveData<FetchStatus> liveData = emailViewModel.deleteMessage();
                Context context = this;
                liveData.observe(this, new Observer<FetchStatus>() {
                    @Override
                    public void onChanged(FetchStatus fetchStatus) {
                        if(fetchStatus.equals(FetchStatus.ERROR) || fetchStatus.equals(FetchStatus.DONE)){
                            if(fetchStatus.equals(FetchStatus.ERROR)) Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                            if(fetchStatus.equals(FetchStatus.DONE)) {
                                Toast.makeText(context, R.string.mesage_deleted, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, EmailsActivity.class);
                                context.startActivity(intent);
                            }
                            liveData.removeObserver(this);
                        }
                    }
                });
                break;
            }
            case R.id.draft:{
                emailViewModel.editAsDraft();
                break;
            }
            case R.id.add_tag_menu_item:{
                emailViewModel.addTag();
                break;
            }
            case android.R.id.home: {
                goBack();
                break;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goBack(){
        Intent intent = new Intent(this, EmailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
