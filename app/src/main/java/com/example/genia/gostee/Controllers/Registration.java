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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration extends AppCompatActivity {
    Button btnReg;
    EditText edContact, edPassword, edPasswordRepeat,  edName;
    String contact, password, passwordRepeat, name;
    ConnToDB connToDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        btnReg = (Button) findViewById(R.id.btnReg);

        edContact = (EditText) findViewById(R.id.edContact);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edPasswordRepeat = (EditText) findViewById(R.id.edPasswordRepeat);
        edName = (EditText) findViewById(R.id.edName);
        connToDB = new ConnToDB();
        contact = "";
        password = "";
        passwordRepeat = "";
        name = "";

        OnClickListener onClickListener = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
              switch (view.getId()){

                  case R.id.btnReg:
                      registration();
                      break;
              }
            }
        };
        btnReg.setOnClickListener(onClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void registration() {
        Log.i("Registration", "Кнопка регистрации");
        contact = edContact.getText().toString();
        password = edPassword.getText().toString();
        passwordRepeat = edPasswordRepeat.getText().toString();
        name = edName.getText().toString();
        if (contact.isEmpty() || password.isEmpty() || name.isEmpty()){

            Toast.makeText(getApplicationContext(),
                    "Заполните все поля", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (!isEmailValid(contact) && !isPhoneNumberValid(contact)){
            Toast.makeText(getApplicationContext(),
                    "Введите корректный email или номер телефона", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (password.length() <6){
            Toast.makeText(getApplicationContext(),
                    "Пароль должен содержать не меньше шести символов", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (!password.equals(passwordRepeat)){
            Toast.makeText(getApplicationContext(),
                    "Введенные вами пароли не совпадают.", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (connToDB.registration(contact, password, name, getApplicationContext())) finish();
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
