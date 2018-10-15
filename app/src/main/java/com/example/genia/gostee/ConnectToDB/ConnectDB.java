package com.example.genia.gostee.ConnectToDB;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ConnectDB extends AsyncTask<Object, Object, String>
{
    private String input, password, login;
    @SuppressLint("StaticFieldLeak")
    private Button button = null;
    private String ansver;
    private HttpURLConnection conn;
    private String SERVER_NAME = "http://r2551241.beget.tech";
    private int num;

    public ConnectDB(){

    }

    ConnectDB(String _input){
        input = _input;
    }
    ConnectDB(String _input, Button btn){
        input = _input;
        button = btn;
    }

    public ConnectDB(String _password, String _login, Button _button, int _num) {
        button = _button;
        password = _password;
        login = _login;
        num = _num;
    }


   @SuppressLint("SetTextI18n")
   @Override
    protected void onPreExecute() {
        button.setText("Block");
        button.setEnabled(false);
        super.onPreExecute();
    }



    @Override
    protected String doInBackground(Object... voids) {
        try {

            switch (num){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    Log.i("ConnToDB",
                            "registration - записываем в базу временный пароль");
                    Log.i("ConnToDB",
                            "Временный пароль: " + password);
                    StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                    String encryptedPassword = passwordEncryptor.encryptPassword(password);
                    try {
                        input = SERVER_NAME
                                + "/gosteeRecoveryPassword.php?action=temporaryPassword&login="
                                + URLEncoder.encode(login, "UTF-8")
                                + "&encryptedPassword="
                                + URLEncoder.encode(encryptedPassword, "UTF-8")
                                + "&password="
                                + URLEncoder.encode(password, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
            }



            Log.i("ConnectDB",
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
            Log.i("chat", "+ MainActivity - answer from server (200 = ОК): "
                    + res.toString());

        } catch (Exception e) {
            Log.i("chat",
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
            Log.i("chat", "+ FoneService - Full answer from server: "
                    + sb.toString());
            ansver = sb.toString();
            ansver = ansver.substring(ansver.indexOf("["), ansver.indexOf("]") + 1);

            Log.i("chat", "+ FoneService answer: " + ansver);

            is.close();
            br.close();
        }
        catch (Exception e) {
            Log.i("chat", "+ FoneService error: " + e.getMessage());
        }
        finally {
            conn.disconnect();
        }
        return ansver;

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(String s) {
        Log.i("ConnectDB","Unblock");
        //button.setText("Unblock");
        button.setEnabled(true);
        super.onPostExecute(s);
    }
}
