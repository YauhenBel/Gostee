package com.example.genia.gostee.Controllers;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genia.gostee.ConnToDB.ConnDB;
import com.example.genia.gostee.R;
import com.google.zxing.WriterException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class CreateQRCode extends AppCompatActivity {

    private static final String TAG = "CreateQRCode";
    QRGEncoder qrgEncoder;
    ImageView imvQRCode;
    String mUserId = null;
    Bitmap bitmap;
    String input = "";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("info", MODE_PRIVATE);
        mUserId = preferences.getString("userId", "");
        setContentView(R.layout.activity_create_qrcode);
        imvQRCode = (ImageView) findViewById(R.id.imvQRCode);
        //bundle = getIntent().getExtras();
        //mUserId = bundle.getString("idUser");
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        qrgEncoder = new QRGEncoder(mUserId, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        imvQRCode.setImageBitmap(bitmap);

        MyThread myThread = new MyThread();
        myThread.start();
    }

    private void changeStatusScan(){
            String SERVER_NAME = "http://r2551241.beget.tech";
        try {
            input = SERVER_NAME
                    + "/gostee.php?action=changeStatusScan&idUser="
                    + URLEncoder.encode(mUserId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ConnDB connDB = new ConnDB();
        String ansver = connDB.sendRequest(input, this);
        Log.i(TAG, "updateDB: ansver: " +ansver);
    }

    private String checkStatusScan(){
        String SERVER_NAME = "http://r2551241.beget.tech";
        try {
            input = SERVER_NAME
                    + "/gostee.php?action=checkStatusScan&idUser="
                    + URLEncoder.encode(mUserId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ConnDB connDB = new ConnDB();
        String ansver = connDB.sendRequest(input, this);
        Log.i(TAG, "updateDB: ansver: " +ansver);
        return ansver;
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            changeStatusScan();
            for (int i = 0; i<60; i++){
                if (checkStatusScan().equals("0")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("statusScan", true);
                    editor.apply();
                    finish();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            finish();
        }
    }

    public void goBack(View view){finish();}
}
