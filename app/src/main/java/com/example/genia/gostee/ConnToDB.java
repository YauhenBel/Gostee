package com.example.genia.gostee;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class ConnToDB {

    public Boolean authorization(String mLogin, String mPassword){
        ConnectDB connectDB = null;
        String ansver;
        try {
            String server_name = "http://r2551241.beget.tech";
            String input = server_name
                    + "/gostee.php?action=input&login="
                    + URLEncoder.encode(mLogin, "UTF-8")
                    +"&password="
                    +URLEncoder.encode(mPassword, "UTF-8");
            connectDB = new ConnectDB(input);
            connectDB.execute();
            ansver =  connectDB.get();
            if (ansver != null && !ansver.isEmpty()) {
                Log.i("ConnDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);
                try {
                    ansver = ansver.substring(ansver.indexOf("{"), ansver.indexOf("}") + 1);
                    JSONObject jo = new JSONObject(ansver);

                    Log.i("chat","=================>>> "
                                    + jo.getString("login") + " "
                                    + jo.getString("password"));
                    return true;
                }
                catch (Exception e) {
                    Log.i("chat",
                            "+ ConnDB ---------- server response error:\n"
                                    + e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean registration (String contact, String password, String name){
        ConnectDB connectDB = null;
        String ansver;
        try {
            String server_name = "http://r2551241.beget.tech";
            String input = server_name
                    + "/gostee.php?action=reg&login="
                    + URLEncoder.encode(contact, "UTF-8")
                    +"&password="
                    +URLEncoder.encode(password, "UTF-8")
                    +"&name="
                    +URLEncoder.encode(name, "UTF-8");
            connectDB = new ConnectDB(input);
            connectDB.execute();
            ansver =  connectDB.get();
            if (ansver != null && !ansver.isEmpty()) {
                Log.i("ConnDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);
                return true;
            }
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


    private class ConnectDB extends AsyncTask<Object, Object, String>
    {
        String input;
        ConnectDB(String _input){
            input = _input;
        }
        String ansver;
        HttpURLConnection conn;
        

        @Override
        protected String doInBackground(Object... voids) {
            try {


                Log.i("chat",
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
                Log.i("chat", "+ FoneService - Full answer from server:\n"
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
    }
}
