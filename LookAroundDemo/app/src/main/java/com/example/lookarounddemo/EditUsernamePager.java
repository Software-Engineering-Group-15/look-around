package com.example.lookarounddemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.lookarounddemo.widget.ItemGroup;

import org.jetbrains.annotations.Nullable;

public class EditUsernamePager extends Fragment implements View.OnClickListener {
    Button bu_edit;
    private EditText edit_name;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.pager_edit_username,null);
        return layout;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edit_name = (EditText)getActivity().findViewById(R.id.et_edit_name);
        //edit_name.setText(((ItemGroup)getActivity().findViewById(R.id.ig_name)).getContentEdt().getText());
        bu_edit = (Button)getActivity().findViewById(R.id.edit_button);
        bu_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControllerActivity activity = (ControllerActivity) getActivity();
                activity.setCurrentItem(1);
            }
        });
    }
    @Override
    public void onClick(View v) {

    }
}
