package com.example.genia.gostee.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genia.gostee.ConnectToDB.ConnToDB;
import com.example.genia.gostee.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button btnLogIn;
    EditText etLogin, etPassword;
    TextView tvRegistration, tvRecovery;
    SharedPreferences sharedPreferences;
    ConnToDB connToDB;
    Editor ed;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegistration = (TextView) findViewById(R.id.tvRegistarton);
        tvRecovery = (TextView) findViewById(R.id.tvRecovery);
        connToDB = new ConnToDB();
        sharedPreferences = getPreferences(MODE_PRIVATE);
        ed = sharedPreferences.edit();

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnLogIn:
                        userLogIn();
                        break;
                    case R.id.tvRegistarton:
                        goToRegistration();
                        break;
                    case R.id.tvRecovery:
                        goToRecovery();
                        break;
                }
            }
        };

        btnLogIn.setOnClickListener(onClickListener);
        tvRegistration.setOnClickListener(onClickListener);
        tvRecovery.setOnClickListener(onClickListener);
    }

    @SuppressLint("ResourceAsColor")
    private void userLogIn() {
        Log.i("MainActivity", "Authorization");
        String editLogin = etLogin.getText().toString();
        String editPassword = etPassword.getText().toString();
        if (editLogin.isEmpty() || editPassword.isEmpty()) {
            Log.i("MainActivity", "Authorization2");
            Toast.makeText(getApplicationContext(),
                    "Заполните все поля.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
            Log.i("MainActivity", "Authorization1");
            btnLogIn.setEnabled(false);
            if (connToDB.getUserInformation(editLogin, editPassword, ed)) {
                if (sharedPreferences.getString("status", "").equals("0")) {
                    goToMainWorkScreen();
                }else {
                    goToCreateNewPassword(sharedPreferences.getString("id", ""));
                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Неправильный логин или пароль", Toast.LENGTH_SHORT)
                        .show();

            }
        btnLogIn.setEnabled(true);
    }


    private void goToMainWorkScreen(){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    private void goToCreateNewPassword(String id){
        Intent intent = new Intent(this, NewPassword.class);
        intent.putExtra("id", id);
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
