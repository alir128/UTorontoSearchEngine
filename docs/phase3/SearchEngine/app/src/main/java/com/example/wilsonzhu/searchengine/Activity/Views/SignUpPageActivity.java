package com.example.wilsonzhu.searchengine.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilsonzhu.searchengine.R;

import java.text.ParseException;

public class SignUpPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
    }

    public void createClient(View view) {
        EditText passwordBoxOne = (EditText) findViewById(R.id.activity_sign_up_password_edit_text);
        EditText passwordBoxTwo = (EditText) findViewById(R.id.SignupPasswordConfirmEditText);
        EditText userName = (EditText) findViewById(R.id.SignupUserNameEditText);
        EditText userEmail = (EditText) findViewById(R.id.activity_sign_up_input_email_text_view);
        EditText groupCourse = (EditText) findViewById(R.id.SignupGroupEditText);
        if ((passwordBoxOne.getText().toString().equals("") || passwordBoxTwo.getText().toString().equals("")
                || userName.getText().toString().equals("")|| userEmail.getText().toString().equals("")
                || groupCourse.getText().toString().equals(""))) {
            Context context = getApplicationContext();
            CharSequence text = "Required part can not be empty";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else if (!passwordBoxOne.getText().toString().equals(passwordBoxTwo.getText().toString())) {
            Context context = getApplicationContext();
            CharSequence text = "Passwords not match";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else{
            Context context = getApplicationContext();
            CharSequence text = "Create Successfully";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
