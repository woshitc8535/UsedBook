package com.laioffer.usedbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private Button login;
    private TextView toRegister;

    private TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }




    private void init() {
        mEmail = findViewById(R.id.text_mail);
        mPassword = findViewById(R.id.text_password);
        login = findViewById(R.id.btn_login);
        toRegister = findViewById(R.id.toRegister);
        forgot = findViewById(R.id.text_forgot);
    }
}
