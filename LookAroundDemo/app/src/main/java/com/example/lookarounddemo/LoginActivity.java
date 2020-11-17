package com.example.lookarounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login_finish(View view){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void forget_password(View view){
        Intent intent = new Intent(this, FindPasswordActivity.class);
        startActivity(intent);
    }
}