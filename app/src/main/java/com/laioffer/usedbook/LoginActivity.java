package com.laioffer.usedbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private Button login_btn;


    private TextView toRegister;

    //auto login
    FirebaseUser firebaseUser;

    private TextView forgot;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");


        init();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(getApplicationContext(), ControlPannel.class);
            startActivity(intent);
            finish();
        }

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = mEmail.getText().toString();
                String text_pssword = mPassword.getText().toString();

                if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_pssword)) {
                    Toast.makeText(LoginActivity.this, "All the field are needed", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(text_email, text_pssword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, ControlPannel.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Failed. Please register.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }


    private void init() {
        mEmail = findViewById(R.id.text_mail);
        mPassword = findViewById(R.id.text_password);
        login_btn = findViewById(R.id.btn_login);
        toRegister = findViewById(R.id.toRegister);
        forgot = findViewById(R.id.text_forgot);
        auth = FirebaseAuth.getInstance();
    }
}
