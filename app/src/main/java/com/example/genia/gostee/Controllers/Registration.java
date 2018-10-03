package com.example.genia.gostee.Controllers;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.genia.gostee.ConnectToDB.ConnToDB;
import com.example.genia.gostee.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.jasypt.util.password.StrongPasswordEncryptor;

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
        final ConnToDB connToDB = new ConnToDB();
        final String[] contact = {""};
        final String[] password = {""};
        final String[] name = {""};

        OnClickListener onClickListener = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
              switch (view.getId()){
                  case R.id.btnRegistration:
                      contact[0] = edContact.getText().toString();
                      password[0] = edPassword.getText().toString();
                      name[0] = edName.getText().toString();
                      if (contact[0].isEmpty() || password[0].isEmpty() || name[0].isEmpty()){

                          Toast.makeText(getApplicationContext(),
                                  "Заполните все поля", Toast.LENGTH_SHORT)
                                  .show();
                          break;
                      }
                      if (!isEmailValid(contact[0]) && !isPhoneNumberValid(contact[0])){
                          Toast.makeText(getApplicationContext(),
                                  "Введите корректный email или номер телефона", Toast.LENGTH_LONG)
                                  .show();
                          break;
                      }
                      if (password[0].length() <6){
                          Toast.makeText(getApplicationContext(),
                                  "Пароль должен содержать не меньше шести символов", Toast.LENGTH_LONG)
                                  .show();
                          break;
                      }
                      if (connToDB.checkData(contact[0])){
                          Log.i("Registration", "Логин оригинальный");
                          new Thread(new Runnable() {
                              public void run() {
                                  connToDB.registration(contact[0], password[0], name[0]);

                              }
                          }).start();
                          finish();
                      }else {
                          Log.i("Registration", "Пользователь с таким логином уже есть");
                          Toast.makeText(getApplicationContext(),
                                  "Пользователь с таким логином уже существует", Toast.LENGTH_LONG)
                                  .show();
                      }




                      break;
              }
            }
        };
        btnReg.setOnClickListener(onClickListener);
    }



    private boolean isPhoneNumberValid(String number){
        Log.i("Registration", "isPhoneNumberValid");
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(number, "");
            if (phoneUtil.isValidNumber(phoneNumber)){
                return true;
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean isEmailValid(String email) {
        Log.i("Registration", "isEmailValid");
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
