package com.example.genia.gostee.ConnectToDB;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class ConnToDB {

    ConnectDB connectDB = null;
    String ansver = "";
    String SERVER_NAME = "http://r2551241.beget.tech";

    public Boolean getUserInformation(String mLogin, String mPassword, Editor ed){
        try {
            String input = SERVER_NAME
                    + "/gostee.php?action=getUserInformation&login="
                    + URLEncoder.encode(mLogin, "UTF-8");
            connectDB = new ConnectDB(input);
            connectDB.execute();
            ansver =  connectDB.get();
            if (ansver != null && !ansver.isEmpty()) {
               // Log.i("ConnDB", "+ Connect ---------- reply contains JSON:" + ansver);
                try {
                    ansver = ansver.substring(ansver.indexOf("{"), ansver.indexOf("}") + 1);
                    JSONObject jo = new JSONObject(ansver);

                    /*Log.i("chat","=================>>> \n"
                                    + "ID - " + jo.getString("id") + " \n"
                                    + "Логин - " + jo.getString("login") + " \n"
                                    + "Пароль - " +jo.getString("password") + " "
                                    + "Имя - " +jo.getString("name") + " \n"
                                    + "Статус восстановления - " +jo.getString("statusRecovery"));*/
                    StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                    if (passwordEncryptor.checkPassword(mPassword, jo.getString("password"))){
                        //Log.i("Registration", "Пароли совпадают.");
                        ed.putString("id", jo.getString("id"));
                        ed.putString("status", jo.getString("statusRecovery"));
                        ed.commit();
                        return true;
                    }else{
                        //Log.i("Registration", "Пароли не совпадают.");
                        return false;
                    }

                }
                catch (Exception e) {
                    Log.i("ConnToDB",
                            "+ ConnToDB ---------- server response error:\n"
                                    + e.getMessage());
                }
            }
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Boolean registration (String contact, String password, String name, Context context){
        Log.i("ConnToDB",
                "registration - регистрируемся в базе");

        Log.i("ConnToDB",
                "Пароль в базу: " + password);
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        try {
            String input = SERVER_NAME
                    + "/gostee.php?action=reg&login="
                    + URLEncoder.encode(contact, "UTF-8")
                    +"&password="
                    +URLEncoder.encode(encryptedPassword, "UTF-8")
                    +"&name="
                    +URLEncoder.encode(name, "UTF-8");
            connectDB = new ConnectDB(input);
            connectDB.execute();
            ansver =  connectDB.get();

            if (ansver != null && !ansver.isEmpty()) {
                Log.i("ConnToDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);
                if (ansver.equals("1")){
                    Log.i("ConnToDB", "Пользователь с таким логином уже существует");
                    Toast.makeText(context,
                            "Пользователь с таким логином уже существует", Toast.LENGTH_LONG)
                            .show();
                    return false;
                }
                return true;
            }
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean temporaryPassword(String password, String login, Button button){
        Log.i("ConnToDB",
                "registration - записываем в базу временный пароль");
        Log.i("ConnToDB",
                "Временный пароль: " + password);
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        try {
            String input = SERVER_NAME
                    + "/gosteeRecoveryPassword.php?action=temporaryPassword&login="
                    + URLEncoder.encode(login, "UTF-8")
                    +"&encryptedPassword="
                    +URLEncoder.encode(encryptedPassword, "UTF-8")
                    +"&password="
                    +URLEncoder.encode(password, "UTF-8");
            connectDB = new ConnectDB(input, button);
            connectDB.execute();
            ansver =  connectDB.get();
            if (ansver != null && !ansver.isEmpty()) {
                Log.i("ConnToDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);
                if (ansver.equals("1")){
                    return true;
                }
                if (ansver.equals("0")){
                   /* Toast.makeText(context,
                            "Пользователь с таким логином не найден", Toast.LENGTH_SHORT)
                            .show();*/
                    return false;
                }
                if (ansver.equals("2")){
                   /* Toast.makeText(context,
                            "Возникла непредвиденная ошибка. Повторите еще раз.", Toast.LENGTH_SHORT)
                            .show();*/
                }
                return true;
            }
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean newPassword(String password, String id){
        Log.i("ConnToDB",
                "registration - записываем в базу временный пароль");
        Log.i("ConnToDB",
                "Временный пароль: " + password);
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        try {
            String server_name = "http://r2551241.beget.tech";
            String input = server_name
                    + "/gosteeRecoveryPassword.php?action=newPassword&id="
                    + URLEncoder.encode(id, "UTF-8")
                    +"&password="
                    +URLEncoder.encode(encryptedPassword, "UTF-8");
            connectDB = new ConnectDB(input);
            connectDB.execute();
            ansver =  connectDB.get();

            if (ansver != null && !ansver.isEmpty()) {
                Log.i("ConnToDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);
                return true;
            }
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;

    }



}
