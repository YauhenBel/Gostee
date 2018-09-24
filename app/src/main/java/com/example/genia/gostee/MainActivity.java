package com.example.genia.gostee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnLogIn;
    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }


    public void LogIn(View view) {
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
        }

    private void goToNewLayout(){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}
