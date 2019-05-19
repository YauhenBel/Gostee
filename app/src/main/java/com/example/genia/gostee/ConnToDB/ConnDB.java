package com.example.genia.gostee.ConnToDB;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

public class ConnDB {

    private String  ansver  = "";
    private HttpURLConnection conn;
    private String TAG = "ConnDB";

    public void Disconnect(){
        conn.disconnect();
    }

    public String sendRequest(String input, final Context context, int timeOut){
        try {

            Log.i("ConnDB",
                    "sendRequest: Send request on the server "
                            + input);
            URL url = new URL(input);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(timeOut);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setDoInput(true);
            conn.connect();
            Integer res = conn.getResponseCode();
            Log.i("ConnDB", "sendRequest: Answer from server (200 = ОК): "
                    + res.toString());

        } catch (UnknownHostException e){
            Log.i("ConnDB",
                    "Нет подключения к сети");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override public void run() {
                    Toast.makeText(context,
                            "Нет подключения к сети", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } catch (SocketTimeoutException e){
            Log.i("ConnDB",
                    "Превышено время ожидания ответа");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override public void run() {
                    Toast.makeText(context,
                            "Превышено время ожидания ответа", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } catch (Exception e ) {
            Log.i("ConnDB",
                    "sendRequest: Answer from server ERROR: "
                            + e.getMessage());
            if (e instanceof InterruptedIOException) {
                Log.i(TAG, "sendRequest: Thread was interrupt");
                Thread.currentThread().interrupt();
            }
           e.printStackTrace();
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
            Log.i("ConnDB", "sendRequest: Full answer from server: "
                    + sb.toString());
            ansver = sb.toString();
            ansver = ansver.substring(ansver.indexOf("[") + 1, ansver.indexOf("]"));

            Log.i("ConnDB", "sendRequest: Answer: " + ansver);

            is.close();
            br.close();
        }
        catch (Exception e) {
            Log.i("sendRequest", "sendRequest: Error: " + e.getMessage());
        }
        finally {
            conn.disconnect();
        }



      return ansver;
    }
}
