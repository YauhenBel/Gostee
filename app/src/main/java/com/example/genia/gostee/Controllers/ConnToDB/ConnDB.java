package com.example.genia.gostee.Controllers.ConnToDB;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnDB {

    private String  ansver  = "";
    private HttpURLConnection conn;

    public String sendRequest(String input){
        try {

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
            ansver = ansver.substring(ansver.indexOf("[") + 1, ansver.indexOf("]"));

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
}