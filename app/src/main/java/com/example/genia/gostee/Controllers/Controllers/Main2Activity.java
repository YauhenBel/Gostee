package com.example.genia.gostee.Controllers.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.genia.gostee.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void goBack(View view) {
        finish();
    }

    public void addNewCard(View view){
        Intent intent = new Intent(this, AddCard.class);
        startActivity(intent);
    }
}
