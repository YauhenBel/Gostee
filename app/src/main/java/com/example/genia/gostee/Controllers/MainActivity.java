package com.example.genia.gostee.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genia.gostee.ConnToDB.ConnDB;
import com.example.genia.gostee.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private Button btnLogIn;
    private EditText etLogin, etPassword;
    private String input = "";
    private ConstraintLayout constraintLayout;
    private String TAG = "MainActivity";
    SharedPreferences sharedPreferences;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        TextView tvRegistration = (TextView) findViewById(R.id.tvRegistarton);
        TextView tvRecovery = (TextView) findViewById(R.id.tvRecovery);
        constraintLayout = (ConstraintLayout) findViewById(R.id.inputProcecc);
        ImageButton regAcGoogle = findViewById(R.id.regAcrossGoogle);
        ImageButton regAcVK = findViewById(R.id.regAcrossVK);


        OnClickListener onClickListener = new OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnLogIn:
                        new Thread(new Runnable() {
                            @Override public void run() {
                                userLogIn();
                                workWithGui(1);
                            }
                        }).start();
                        break;
                    case R.id.tvRegistarton:
                        goToRegistration();
                        break;
                    case R.id.tvRecovery:
                        goToRecovery();
                        break;
                    case R.id.regAcrossGoogle:
                        Toast.makeText(getApplicationContext(),
                                "Регистрация через Google", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.regAcrossVK:
                        Toast.makeText(getApplicationContext(),
                                "Регистрация через VK", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        btnLogIn.setOnClickListener(onClickListener);
        regAcGoogle.setOnClickListener(onClickListener);
        regAcVK.setOnClickListener(onClickListener);
        tvRegistration.setOnClickListener(onClickListener);
        tvRecovery.setOnClickListener(onClickListener);
    }

    private void workWithGui(final int x){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override public void run() {
                switch (x){
                    case 0:
                        constraintLayout.setVisibility(View.VISIBLE);
                        btnLogIn.setEnabled(false);
                        break;
                    case 1:
                        constraintLayout.setVisibility(View.INVISIBLE);
                        btnLogIn.setEnabled(true);
                        break;
                    case 2:
                        Log.i("Registration", "Пароли не совпадают.");
                        Toast.makeText(getApplicationContext(),
                                "Неправильный логин или пароль", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),
                                "Заполните все поля.", Toast.LENGTH_SHORT)
                                .show();
                        break;

                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void userLogIn() {
        Log.i("MainActivity", "Authorization");
        String editLogin = etLogin.getText().toString().trim();
        String editPassword = etPassword.getText().toString();

        if (editLogin.isEmpty() || editPassword.isEmpty()) {
            Log.i("MainActivity", "Authorization2");
            workWithGui(3);
            return;
        }
        workWithGui(0);
        Log.i("MainActivity", "Authorization1");

        try {
            String SERVER_NAME = "http://r2551241.beget.tech";
            input = SERVER_NAME
                    + "/gostee.php?action=getUserInformation&login="
                    + URLEncoder.encode(editLogin, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ConnDB connDB = new ConnDB();
        String ansver = connDB.sendRequest(input, this, 10000);

        if (ansver != null && !ansver.isEmpty()) {
            Log.i("ConnDB", "+ Connect ---------- reply contains JSON:" + ansver);
            try {
                Log.i("userLogIn", " - answer: " + ansver);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(ansver);

                JsonNode idNode = jsonNode.path("id");
                String mUserID = idNode.asText();
                JsonNode nameNode = jsonNode.path("name");
                String userName = nameNode.asText();
                JsonNode passwordNode = jsonNode.path("password");
                JsonNode statusNode = jsonNode.path("statusRecovery");

                sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
                Editor editor = sharedPreferences.edit();
                editor.putString("userId", mUserID);
                editor.putString("userName", userName);
                editor.putBoolean("statusADD", false);
                editor.putBoolean("statusScan", false);
                editor.putString("idsCards", "");
                editor.apply();

                StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                if (passwordEncryptor.checkPassword(editPassword, passwordNode.asText())){
                    Log.i("MainActivity", "Пароли совпадают.");

                    if (statusNode.asText().equals("0")) {
                        goToMainWorkScreen();
                    }else {
                        goToCreateNewPassword(idNode.asText(), nameNode.asText());
                    }
                }else{
                   workWithGui(2);
                }

            }
            catch (Exception e) {
                workWithGui(2);
            }
        }
    }


    private void goToMainWorkScreen(){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    private void goToCreateNewPassword(String id, String userName){
        Intent intent = new Intent(this, NewPassword.class);
        startActivity(intent);
    }

    private void goToRecovery(){
        Intent intent = new Intent(this, RecoveryPassword.class);
        startActivity(intent);
    }

    private void goToRegistration(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }
}
