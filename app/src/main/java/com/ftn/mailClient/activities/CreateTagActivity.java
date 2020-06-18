package com.ftn.mailClient.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.retrofit.TagApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTagActivity extends AppCompatActivity {
    private TextView tagName;
    private String tagNameString;

    private Tag tag;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tag_activity);

        tagName = findViewById(R.id.editTag);


        Button bu = findViewById(R.id.buttonForCreateTag);
        bu.setOnClickListener(v -> clickHandler(v));

        Button cancelBu = findViewById(R.id.buttonForCancelCreateTag);
        cancelBu.setOnClickListener(v -> activityEndRedirect());
    }

    private void clickHandler(View v) {
        tagNameString = tagName.getText().toString();
        Tag nTag = new Tag(null, tagNameString, null, null);

        TagApi tagApi = RetrofitClient.getApi(TagApi.class);
        tagApi.saveTag(nTag).enqueue(new Callback<Tag>() {
            @Override
            public void onResponse(Call<Tag> call, Response<Tag> response) {
                if(response.isSuccessful()){
                    activityEndRedirect();
                }
                else Toast.makeText(getBaseContext(), "There was a problem, tag isn't created "+response.code(), Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(Call<Tag> call, Throwable t) {
                Toast.makeText(getBaseContext(), "There was a problem, tag isn't created", Toast.LENGTH_LONG);
            }
        });
    }

    private void activityEndRedirect() {
        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
        startActivity(intent);
    }
}
