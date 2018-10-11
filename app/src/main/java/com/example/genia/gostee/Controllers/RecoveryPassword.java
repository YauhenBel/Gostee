package com.example.genia.gostee.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.genia.gostee.ConnectToDB.ConnToDB;
import com.example.genia.gostee.R;

import java.util.Random;

public class RecoveryPassword extends AppCompatActivity {

    EditText newPassword;
    Button btnSendNewPassword;
    ConnToDB connToDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoverypass);

        newPassword = (EditText) findViewById(R.id.edRecoveryPassword);
        btnSendNewPassword = (Button) findViewById(R.id.btnSendNewPassword);
        connToDB = new ConnToDB();

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnSendNewPassword:
                        createNewPassword();
                        break;
                }
            }
        };

        btnSendNewPassword.setOnClickListener(onClickListener);
    }

    private void createNewPassword() {
        String login = newPassword.getText().toString();
        String newPassword = generateString(10);

        Log.i("RecoveryPassword", "Проверяем логин...");
        if (connToDB.checkData(login)){
            Log.i("RecoveryPassword", "Пользователь с таким логином не найден.");
            Toast.makeText(getApplicationContext(),
                    "Пользователь с таким логином не найден", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Log.i("RecoveryPassword", "Записываем в базу пароль...");
        if (!connToDB.newPassword(newPassword, login)){
            Log.i("RecoveryPassword", "Ошибка записи пароля в базу.");
            Toast.makeText(getApplicationContext(),
                    "Возникла непредвиденная ошибка. Повторите еще раз.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Log.i("RecoveryPassword", "Отправляем пользователю сообщение с новым паролем.");
        if (connToDB.sendMess(login, newPassword)){
            Log.i("RecoveryPassword", "Пароль доставлен.");
            finish();
        }else{
            Log.i("RecoveryPassword", "Ошибка. Пароль не доставлен.");
            Toast.makeText(getApplicationContext(),
                    "Возникла непредвиденная ошибка. Повторите еще раз.", Toast.LENGTH_SHORT)
                    .show();
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


}
