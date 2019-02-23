package com.example.genia.gostee.Controllers.Controllers;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genia.gostee.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class CreateQRCode extends AppCompatActivity {

    QRGEncoder qrgEncoder;
    ImageView imvQRCode;
    String mUserId = null;
    Bundle bundle = null;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qrcode);
        imvQRCode = (ImageView) findViewById(R.id.imvQRCode);
        bundle = getIntent().getExtras();
        mUserId = bundle.getString("idUser");
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
    }

    public void goBack(View view){finish();}
}
