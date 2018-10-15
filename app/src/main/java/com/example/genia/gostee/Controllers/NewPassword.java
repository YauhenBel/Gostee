package com.example.genia.gostee.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.genia.gostee.R;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class NewPassword extends AppCompatActivity {

    Bundle bundle;
    EditText edNewPassword, edCheckNewPassword;
    Button btnSaveNewPassword;
    String password, check;
    String ansver = "";
    SendNewPasswordToDB sendNewPasswordToDB;
    String encryptedPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        bundle = getIntent().getExtras();
        Log.i("NewPassword", "Номер пользователя: " + bundle.getString("id"));

        edNewPassword = (EditText) findViewById(R.id.edNewPassword);
        edCheckNewPassword = (EditText) findViewById(R.id.edCheckNewPassword);
        btnSaveNewPassword = (Button) findViewById(R.id.btnSaveNewPassword);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnSaveNewPassword:
                        saveNewPassword();
                        break;
                }
            }
        };

        btnSaveNewPassword.setOnClickListener(onClickListener);
    }

    public void goBack(View view) {
        finish();
    }

    private void saveNewPassword(){
        password = edNewPassword.getText().toString();
        check = edCheckNewPassword.getText().toString();
        if (password.isEmpty() || check.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Заполните все поля", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (password.length()<6){
            Toast.makeText(getApplicationContext(),
                    "Пароль должен быть не менее шести символов", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (!password.equals(check)) {
            Toast.makeText(getApplicationContext(),
                    "Пароли не совпадают", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        encryptedPassword = passwordEncryptor.encryptPassword(password);

        sendNewPasswordToDB = new SendNewPasswordToDB();
        sendNewPasswordToDB.execute();
        try {
            sendNewPasswordToDB.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (ansver != null && !ansver.isEmpty()) {
                Log.i("ConnToDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);

        }
        finish();
        goToMainWorkScreen();
    }

    private void goToMainWorkScreen(){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class SendNewPasswordToDB extends AsyncTask<Void, Void, Void>
    {

        HttpURLConnection conn;
        String SERVER_NAME = "http://r2551241.beget.tech";
        String input = "";

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            Log.i("SendRecoveryPassword","Block");
            btnSaveNewPassword.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                try {
                    input = SERVER_NAME
                            + "/gosteeRecoveryPassword.php?action=newPassword&id="
                            + URLEncoder.encode(bundle.getString("id"), "UTF-8")
                            +"&password="
                            +URLEncoder.encode(encryptedPassword, "UTF-8");
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
                Log.i("SendRecoveryPassword", "+ FoneService - Full answer from server: "
                        + sb.toString());
                ansver = sb.toString();
                ansver = ansver.substring(ansver.indexOf("["), ansver.indexOf("]") + 1);

                Log.i("SendRecoveryPassword", "+ FoneService answer: " + ansver);

                is.close();
                br.close();
            }
            catch (Exception e) {
                Log.i("SendRecoveryPassword", "+ FoneService error: " + e.getMessage());
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
            btnSaveNewPassword.setEnabled(true);
            super.onPostExecute(sVoid);
        }

    }
}
