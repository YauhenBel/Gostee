package com.example.genia.gostee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                          if (isEmailValid(contact) || isNumberValid(contact)){

                              Toast.makeText(getApplicationContext(),
                                      "Спасибо", Toast.LENGTH_SHORT)
                                      .show();
                              //ConnToDB connToDB = new ConnToDB();
                              //connToDB.registration(contact, password, name);
                          }else {
                              Toast.makeText(getApplicationContext(),
                                      "Введите корректный email или номер телефона", Toast.LENGTH_LONG)
                                      .show();
                          }


                      } else {
                          Toast.makeText(getApplicationContext(),
                                  "Заполните все поля", Toast.LENGTH_SHORT)
                                  .show();
                      }
                      break;
              }
            }
        };
        btnReg.setOnClickListener(onClickListener);
    }

    private boolean isNumberValid(String num){
        Log.i("MainActivity", "Номер телефона: " + num);
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(num, "");
            return phoneUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailValid(String email) {
        Log.i("MainActivity", "Элетронная почта: " + email);
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }


    public void goBack(View view) {
        finish();
    }
}