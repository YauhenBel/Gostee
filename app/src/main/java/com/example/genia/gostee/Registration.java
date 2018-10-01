package com.example.genia.gostee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    Button btnReg;
    EditText edContact, edPassword, edName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        btnReg = (Button) findViewById(R.id.btnRegistration);

        edContact = (EditText) findViewById(R.id.edContact);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edName = (EditText) findViewById(R.id.edName);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
              switch (view.getId()){
                  case R.id.btnRegistration:
                      String contact = edContact.getText().toString();
                      String password = edPassword.getText().toString();
                      String name = edName.getText().toString();
                      if (!contact.isEmpty() && !password.isEmpty() && !name.isEmpty()){
                          ConnToDB connToDB = new ConnToDB();
                          connToDB.registration(contact, password, name);

                      } else {
                          Toast.makeText(getApplicationContext(),
                                  "Заполните все поля", Toast.LENGTH_SHORT)
                                  .show();
                      }
                      break;
              }
            }
        };





    }

    public void goBack(View view) {
        finish();
    }
}
