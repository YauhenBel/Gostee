package com.example.genia.gostee.Controllers;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.genia.gostee.R;

public class NewPassword extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        bundle = getIntent().getExtras();
        Log.i("NewPassword", "Номер пользователя: " + bundle.getString("id"));


    }
}
