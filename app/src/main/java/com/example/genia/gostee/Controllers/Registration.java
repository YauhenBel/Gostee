package com.example.genia.gostee.Controllers;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.genia.gostee.ConnToDB.ConnDB;
import com.example.genia.gostee.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration extends AppCompatActivity {
    private Button btnReg;
    private EditText edContact, edPassword, edPasswordRepeat,  edName;
    private String contact = "", password = "", passwordRepeat = "", name = "", ansver = "";
    private ConstraintLayout constraintLayout;
    private String input = "";

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
        contact = "";
        password = "";
        passwordRepeat = "";
        name = "";
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        constraintLayout = (ConstraintLayout) findViewById(R.id.regProcecc);



        OnClickListener onClickListener = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
              switch (view.getId()){

                  case R.id.btnReg:

                      new Thread(new Runnable() {
                          @Override public void run() {

                              registration();
                              workWithGui(1);
                              }
                          }).start();

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
            workWithGui(2);
            return;
        }
        if (!isEmailValid(contact) && !isPhoneNumberValid(contact)){
            workWithGui(3);
            return;
        }
        if (password.length() <6){
            workWithGui(4);
            return;
        }
        if (!password.equals(passwordRepeat)){
            workWithGui(5);
            return;
        }

        workWithGui(0);
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        try {
            String SERVER_NAME = "http://r2551241.beget.tech";
            input = SERVER_NAME
                    + "/gostee.php?action=reg&login="
                    + URLEncoder.encode(contact, "UTF-8")
                    +"&password="
                    +URLEncoder.encode(encryptedPassword, "UTF-8")
                    +"&name="
                    +URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ConnDB connDB = new ConnDB();
        ansver = connDB.sendRequest(input, this, 10000);
        if (ansver.equals("1")){
            workWithGui(6);
            return;
        }
        finish();
    }

    private void workWithGui(final int x){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override public void run() {
                switch (x){
                    case 0:
                        constraintLayout.setVisibility(View.VISIBLE);
                        btnReg.setEnabled(false);
                        break;
                    case 1:
                        constraintLayout.setVisibility(View.INVISIBLE);
                        btnReg.setEnabled(true);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(),
                                "Заполните все поля", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),
                                "Введите корректный email или номер телефона", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(),
                                "Пароль должен содержать не меньше шести символов", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(),
                                "Введенные вами пароли не совпадают.", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(),
                                "Пользователь с таким логином уже существует", Toast.LENGTH_LONG)
                                .show();
                        break;

                }
            }
        });
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
