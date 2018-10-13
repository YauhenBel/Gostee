package com.example.genia.gostee.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genia.gostee.ConnectToDB.ConnToDB;
import com.example.genia.gostee.ConnectToDB.ConnectDB;
import com.example.genia.gostee.R;

import java.util.Random;

public class RecoveryPassword extends AppCompatActivity {

    EditText newPassword;
    Button btnSendNewPassword;
    ConnToDB connToDB;
    SharedPreferences.Editor ed;
    View viewBlock;
    Context context;
    TextView textView;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoverypass);

        newPassword = (EditText) findViewById(R.id.edRecoveryPassword);
        btnSendNewPassword = (Button) findViewById(R.id.btnSaveRecoveryPassword);
        viewBlock = (View) findViewById(R.id.viewBlock);
        textView = (TextView) findViewById(R.id.textView4);
        connToDB = new ConnToDB();
        context = getApplicationContext();


        OnClickListener onClickListener = new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                btnSendNewPassword.setEnabled(false);
                switch (view.getId()){
                    case R.id.btnSaveRecoveryPassword:
                        btnSendNewPassword.setEnabled(false);
                        btnSendNewPassword.setClickable(false);
                        textView.setText("Block");

                                createTemporaryPassword();

                        btnSendNewPassword.setEnabled(true);
                        btnSendNewPassword.setClickable(true);
                        textView.setText("Unblock");

                        break;
                }

            }
        };

        btnSendNewPassword.setOnClickListener(onClickListener);
    }

    private void createTemporaryPassword() {

        String login = newPassword.getText().toString();
        String newPassword = generateString(10);
        connToDB.temporaryPassword(newPassword, login, btnSendNewPassword);


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


}
