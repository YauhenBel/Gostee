package com.example.genia.gostee.ConnectToDB;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectDB extends AsyncTask<Object, Object, String>
{
    String input;
    Button button;

    ConnectDB(String _input){
        input = _input;
    }
    ConnectDB(String _input, Button btn){
        input = _input;
        button = btn;
    }
    String ansver;
    HttpURLConnection conn;

    @Override
    protected void onPreExecute() {
        button.setEnabled(false);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        button.setEnabled(true);
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(Object... voids) {
        try {


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
}
