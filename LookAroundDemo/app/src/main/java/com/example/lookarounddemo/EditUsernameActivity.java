package com.example.lookarounddemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lookarounddemo.data.User;
import com.example.lookarounddemo.widget.ItemGroup;

public class EditUsernameActivity extends AppCompatActivity {
    private EditText edit_name;
    private Button edit_button;
    private ItemGroup name;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_edit_username);
        name = (ItemGroup) findViewById(R.id.ig_name);
        LayoutInflater factory = LayoutInflater.from(EditUsernameActivity.this);

        View layout = factory.inflate(R.layout.persona_info_page, null);

        ItemGroup it = (ItemGroup) layout.findViewById(R.id.ig_name);
        edit_name = (EditText) findViewById(R.id.et_edit_name);
        //edit_name.setText(it.getContentEdt().getHint());
        edit_button = (Button) findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.setName(edit_name.getText().toString());
                Intent intent = new Intent(EditUsernameActivity.this, ControllerActivity.class);
                startActivity(intent);
            }
        });
    }
}
