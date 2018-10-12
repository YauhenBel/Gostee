package com.example.genia.gostee.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.genia.gostee.ConnectToDB.ConnToDB;
import com.example.genia.gostee.R;

public class NewPassword extends AppCompatActivity {

    Bundle bundle;
    EditText edNewPassword, edCheckNewPassword;
    Button btnSaveNewPassword;
    ConnToDB connToDB;
    String password, check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        bundle = getIntent().getExtras();
        Log.i("NewPassword", "Номер пользователя: " + bundle.getString("id"));

        edNewPassword = (EditText) findViewById(R.id.edNewPassword);
        edCheckNewPassword = (EditText) findViewById(R.id.edCheckNewPassword);
        btnSaveNewPassword = (Button) findViewById(R.id.btnSaveNewPassword);
        connToDB = new ConnToDB();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnSaveNewPassword:
                        password = edNewPassword.getText().toString();
                        check = edCheckNewPassword.getText().toString();
                        if (password.length()<6){
                            Toast.makeText(getApplicationContext(),
                                    "Пароль должен быть не менее шести символов", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        }
                        if (!password.equals(check)) {
                            Toast.makeText(getApplicationContext(),
                                    "Пароли не совпадают", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        }
                        connToDB.newPassword(password, bundle.getString("id"));
                        finish();
                        goToMainWorkScreen();
                        break;
                }
            }
        };

        btnSaveNewPassword.setOnClickListener(onClickListener);
    }

    private void goToMainWorkScreen(){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}
