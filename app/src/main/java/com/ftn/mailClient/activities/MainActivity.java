package com.ftn.mailClient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ftn.mailClient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
            //loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginActivityIntent);
        }
    };

}
