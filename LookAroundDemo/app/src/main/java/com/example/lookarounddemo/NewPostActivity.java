package com.example.lookarounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewPostActivity extends AppCompatActivity {

    private EditText postText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        postText = (EditText) findViewById(R.id.new_post_txt);
    }

    public void map(View view){
        Intent intent = new Intent(NewPostActivity.this, ControllerActivity.class);
        intent.putExtra("new_post",1);
        startActivity(intent);
    }

}