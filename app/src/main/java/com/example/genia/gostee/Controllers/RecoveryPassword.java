package com.example.genia.gostee.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
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

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class RecoveryPassword extends AppCompatActivity {

    EditText newPassword;
    Button btnSendNewPassword;
    Context context;
    TextView textView;
    String ansver = "", login = "", temporaryPassword = "" ;
    String input = "";
    HttpURLConnection conn;
    String SERVER_NAME = "http://r2551241.beget.tech";



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoverypass);

        newPassword = (EditText) findViewById(R.id.edRecoveryPassword);
        btnSendNewPassword = (Button) findViewById(R.id.btnSaveRecoveryPassword);
        textView = (TextView) findViewById(R.id.textView4);

        context = getApplicationContext();

        OnClickListener onClickListener = new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnSaveRecoveryPassword:
                                createTemporaryPassword();
                        break;
                }
            }
        };

        btnSendNewPassword.setOnClickListener(onClickListener);
    }

    private void createTemporaryPassword() {
        Log.i("ConnToDB","createTemporaryPassword");
        login = newPassword.getText().toString();
        temporaryPassword = generateString(10);
        SendRecoveryPassword sendRecoveryPassword = new SendRecoveryPassword();
        sendRecoveryPassword.execute();


        sendRecoveryPassword.get();

            Log.i("ConnToDB","createTemporaryPassword4");


        if (ansver != null && !ansver.isEmpty()) {
            Log.i("ConnToDB",
                    "+ Connect ---------- reply contains JSON:" + ansver);
            if (ansver.equals("1")){

                finish();
            }
            if (ansver.equals("0")){
                Toast.makeText(context,
                            "Пользователь с таким логином не найден", Toast.LENGTH_SHORT)
                            .show();
            }
            if (ansver.equals("2")){
                   Toast.makeText(context,
                            "Возникла непредвиденная ошибка. Повторите еще раз.", Toast.LENGTH_SHORT)
                            .show();
            }
        }

    }

    public static String generateString(int length)
    {
        String RANDSTRING = "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx0123456789";
        Random ran = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = RANDSTRING.charAt(ran.nextInt(RANDSTRING.length()));
        }
        return new String(text);
    }

    @SuppressLint("StaticFieldLeak")
    public class SendRecoveryPassword extends AsyncTask<Object, Object, String>
    {


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            Log.i("SendRecoveryPassword","Block");
            btnSendNewPassword.setText("Block");
            btnSendNewPassword.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... voids) {
            try {
                        Log.i("SendRecoveryPassword",
                                "registration - записываем в базу временный пароль");
                        Log.i("SendRecoveryPassword",
                                "Временный пароль: " + temporaryPassword);
                        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                        String encryptedPassword = passwordEncryptor.encryptPassword(temporaryPassword);
                        try {
                            input = SERVER_NAME
                                    + "/gosteeRecoveryPassword.php?action=temporaryPassword&login="
                                    + URLEncoder.encode(login, "UTF-8")
                                    + "&encryptedPassword="
                                    + URLEncoder.encode(encryptedPassword, "UTF-8")
                                    + "&password="
                                    + URLEncoder.encode(temporaryPassword, "UTF-8");
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
            return ansver;

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            Log.i("SendRecoveryPassword","Unblock");
            btnSendNewPassword.setText("Unblock");
            btnSendNewPassword.setEnabled(true);
           super.onPostExecute(s);
        }
    }
}
