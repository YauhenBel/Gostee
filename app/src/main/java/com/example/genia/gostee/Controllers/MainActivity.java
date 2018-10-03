package com.example.genia.gostee.Controllers;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genia.gostee.ConnectToDB.ConnToDB;
import com.example.genia.gostee.R;

public class MainActivity extends AppCompatActivity {
    Button btnLogIn;
    EditText etLogin, etPassword;
    TextView tvRegistration;
    ImageView logo;
    LinearLayout block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegistration = (TextView) findViewById(R.id.tvRegistarton);
        logo = (ImageView) findViewById(R.id.logo);
        block = (LinearLayout) findViewById(R.id.block2);

        etLogin.setCursorVisible(false);
        etPassword.setCursorVisible(false);



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
                            ConnToDB connToDB = new ConnToDB();
                            if (connToDB.authorization(etLogin.getText().toString(),
                                    etPassword.getText().toString())) {
                                goToNewLayout();
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


                }

            }
        };

        btnLogIn.setOnClickListener(onClickListener);
        tvRegistration.setOnClickListener(onClickListener);
    }


    /*public void LogIn(View view) {
        Log.i("MainActivity", "Authorization");
        if (!etLogin.getText().toString().isEmpty()
                && !etPassword.getText().toString().isEmpty()) {
            Log.i("MainActivity", "Authorization1");
            btnLogIn.setEnabled(false);
            ConnToDB connToDB = new ConnToDB();
            if (connToDB.makeQuery(etLogin.getText().toString(),
                    etPassword.getText().toString())) {
                goToNewLayout();
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
                    "Заполните поля.", Toast.LENGTH_SHORT)
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
        }*/

    private void goToNewLayout(){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    /*public void registration(View view) {

    }*/

    private void goToRegistration(){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }
}