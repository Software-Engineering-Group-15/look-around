package com.example.lookarounddemo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public class LoginFailedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_failed);
    }
    public void goback(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
