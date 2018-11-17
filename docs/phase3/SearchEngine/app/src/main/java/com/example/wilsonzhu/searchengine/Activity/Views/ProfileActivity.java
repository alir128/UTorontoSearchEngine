package com.example.wilsonzhu.searchengine.Activity.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wilsonzhu.searchengine.R;

import java.text.ParseException;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);
    }

    public void profileChange(View view) {
        EditText userName = (EditText) findViewById(R.id.activity_user_name_edit_text);
        EditText userEmail = (EditText) findViewById(R.id.activity_email_edit_text);
        EditText groupCourse = (EditText) findViewById(R.id.activity_group_edit_text);
        if ((userName.getText().toString().equals("")|| userEmail.getText().toString().equals("")
                || groupCourse.getText().toString().equals(""))) {
            Context context = getApplicationContext();
            CharSequence text = "Required part can not be empty";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else{
            Context context = getApplicationContext();
            CharSequence text = "Change Successfully";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
