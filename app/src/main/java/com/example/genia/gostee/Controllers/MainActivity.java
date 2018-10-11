package com.example.genia.gostee.Controllers;

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

public class MainActivity extends AppCompatActivity {
    Button btnLogIn;
    EditText etLogin, etPassword;
    TextView tvRegistration, tvRecovery;
    SharedPreferences sharedPreferences;
    ConnToDB connToDB;
    Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
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
                        Log.i("MainActivity", "Authorization");
                        if (!etLogin.getText().toString().isEmpty()
                                && !etPassword.getText().toString().isEmpty()) {
                            Log.i("MainActivity", "Authorization1");
                            btnLogIn.setEnabled(false);

                            if (connToDB.authorization(etLogin.getText().toString(),
                                    etPassword.getText().toString(), ed)) {
                                Log.i("MainActivity", "Информация: \n" +
                                "id = " + sharedPreferences.getString("id", "") + "\n"
                                + "status = " + sharedPreferences.getString("status", ""));
                                if (sharedPreferences.getString("status", "").equals("0")) {
                                    goToMainWorkScreen();
                                }else {

                                }
                                btnLogIn.setEnabled(true);

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Неправильный логин или пароль", Toast.LENGTH_SHORT)
                                        .show();
                                btnLogIn.setEnabled(true);
                            }


                        }else if (etLogin.getText().toString().isEmpty()
                                && etPassword.getText().toString().isEmpty()) {
                            Log.i("MainActivity", "Authorization2");
                            Toast.makeText(getApplicationContext(),
                                    "Заполните все поля.", Toast.LENGTH_SHORT)
                                    .show();

                        }else if (etLogin.getText().toString().isEmpty()) {
                            Log.i("MainActivity", "Authorization3");
                            Toast.makeText(getApplicationContext(),
                                    "Введите логин.", Toast.LENGTH_SHORT)
                                    .show();

                        }else if (etPassword.getText().toString().isEmpty()) {
                            Log.i("MainActivity", "Authorization4");
                            Toast.makeText(getApplicationContext(),
                                    "Введите пароль.", Toast.LENGTH_SHORT)
                                    .show();

                        }
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


    private void goToMainWorkScreen(){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    private void goToMainWorkScreen(){
        Intent intent = new Intent(this, NewPassword.class);
        startActivity(intent);
    }

    private void goToRecovery(){
        Intent intent = new Intent(this, RecoveryPassword.class);
        startActivity(intent);
    }

    /*public void registration(View view) {

    }*/

    private void goToRegistration(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }
}
