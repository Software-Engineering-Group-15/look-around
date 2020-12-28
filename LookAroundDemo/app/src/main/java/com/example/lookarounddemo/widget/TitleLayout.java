package com.example.lookarounddemo.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lookarounddemo.ControllerActivity;
import com.example.lookarounddemo.R;

public class TitleLayout extends LinearLayout {
    private ImageView iv_backward;
    private TextView tv_title, tv_forward;
    private FragmentManager fragmentManager;
    private FragmentTransaction beginTransaction;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout bar_title = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bar_title, this);
        iv_backward = (ImageView) bar_title.findViewById(R.id.iv_backward);
        tv_title = (TextView) bar_title.findViewById(R.id.title_edt);
        tv_forward = (TextView) bar_title.findViewById(R.id.tv_forward);

        //设置监听器
        //如果点击back则结束活动

    }
}
