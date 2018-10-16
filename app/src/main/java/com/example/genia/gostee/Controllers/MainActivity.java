package com.example.genia.gostee.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genia.gostee.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Button btnLogIn;
    EditText etLogin, etPassword;
    TextView tvRegistration, tvRecovery;
    String ansver = "", input = "", editLogin = "";
    ConnectToAuthorization connectToAuthorization;
    ConstraintLayout constraintLayout;

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
        constraintLayout = (ConstraintLayout) findViewById(R.id.inputProcecc);


        OnClickListener onClickListener = new OnClickListener() {
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
                }
            }
        };

        btnLogIn.setOnClickListener(onClickListener);
        tvRegistration.setOnClickListener(onClickListener);
        tvRecovery.setOnClickListener(onClickListener);
    }

    private void workWithGui(final int x){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override public void run() {
                switch (x){
                    case 0:
                        constraintLayout.setVisibility(View.VISIBLE);
                        //progressBar.setVisibility(View.VISIBLE);
                        btnLogIn.setEnabled(false);
                        //btnReg.setText("Reg");
                        break;
                    case 1:
                        constraintLayout.setVisibility(View.INVISIBLE);
                        //progressBar.setVisibility(View.INVISIBLE);
                        btnLogIn.setEnabled(true);
                        //btnReg.setText("RegFin");
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
        editLogin = etLogin.getText().toString();
        String editPassword = etPassword.getText().toString();

        if (editLogin.isEmpty() || editPassword.isEmpty()) {
            Log.i("MainActivity", "Authorization2");
            workWithGui(3);
            return;
        }
        workWithGui(0);
        Log.i("MainActivity", "Authorization1");
        connectToAuthorization = new ConnectToAuthorization();
        connectToAuthorization.execute();
        try {
            connectToAuthorization.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }

        if (ansver != null && !ansver.isEmpty()) {
            Log.i("ConnDB", "+ Connect ---------- reply contains JSON:" + ansver);
            try {
                Log.i("userLogIn", " - answer: " + ansver);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(ansver);

                JsonNode idNode = jsonNode.path("id");
                JsonNode passwordNode = jsonNode.path("password");
                JsonNode statusNode = jsonNode.path("statusRecovery");

                StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                if (passwordEncryptor.checkPassword(editPassword, passwordNode.asText())){
                    Log.i("Registration", "Пароли совпадают.");

                    if (statusNode.asText().equals("0")) {
                        goToMainWorkScreen();
                    }else {
                        goToCreateNewPassword(idNode.asText());
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

    @SuppressLint("StaticFieldLeak")
    public class ConnectToAuthorization extends AsyncTask<Void, Void, Void>
    {
        HttpURLConnection conn;
        String SERVER_NAME = "http://r2551241.beget.tech";


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            Log.i("SendRecoveryPassword","Block");
            btnLogIn.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                try {
                    input = SERVER_NAME
                            + "/gostee.php?action=getUserInformation&login="
                            + URLEncoder.encode(editLogin, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.i("SendRecoveryPassword",
                        "+ ChatActivity - send request on the server "
                                + input);
                URL url = new URL(input);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setDoInput(true);
                conn.connect();
                Integer res = conn.getResponseCode();
                Log.i("SendRecoveryPassword", "+ MainActivity - answer from server (200 = ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("SendRecoveryPassword",
                        "+ MainActivity - answer from server ERROR: "
                                + e.getMessage());
            }
            try {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bfr_st = null;
                while ((bfr_st = br.readLine()) != null) {
                    sb.append(bfr_st);
                }
                Log.i("SendRecoveryPassword", " - Full answer from server: "
                        + sb.toString());
                ansver = sb.toString();
                ansver = ansver.substring(ansver.indexOf("[") + 1, ansver.indexOf("]"));
                Log.i("SendRecoveryPassword", " - answer: " + ansver);

                is.close();
                br.close();
            }
            catch (Exception e) {
                Log.i("SendRecoveryPassword", " - error: " + e.getMessage());
            }
            finally {
                conn.disconnect();
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void sVoid) {
            Log.i("SendRecoveryPassword","Unblock");
            btnLogIn.setEnabled(true);
            super.onPostExecute(sVoid);
        }

    }
}
