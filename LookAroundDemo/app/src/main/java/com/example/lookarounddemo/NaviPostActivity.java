package com.example.lookarounddemo;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *用于测试的Activity
 *
 *
 */
public class NaviPostActivity extends Activity implements View.OnClickListener {

    private List<Map<String,Object>> lists;
    private SimpleAdapter adapter;
    private ListView listView;

    private ImageView praise_button;
    private ImageView comment_button;
    private boolean ifPraise = false;
    private LinearLayout myComment;
    private boolean ifComment = false;
    private EditText myComment_content;

    private String[] theme = {"张三","李四","王五","张三","李四","王五","张三","李四","王五","张三","李四","王五"};
    private String[] content ={"李在赣神魔","李似神魔恋","经典胡言乱语","张三","李四","王五","张三","李四","王五","张三","李四","王五"};
    private String[] time ={"评论于2020年12月20日15时38分","评论于2020年12月20日15时39分","评论于2020年12月20日15时40分","张三","李四","王五","张三","李四","王五","张三","李四","王五"};
    private int[] imageViews = {R.drawable.im_pub_no_image,R.drawable.im_pub_no_image,R.drawable.im_pub_no_image};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        comment_button = findViewById(R.id.snsBtn);
        praise_button = findViewById(R.id.praiseBtn);
        myComment = findViewById(R.id.myComment);
        myComment_content = findViewById(R.id.comment_content);
        comment_button.setOnClickListener(this);
        praise_button.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.commentList);
//        准备数据源
        lists = new ArrayList<>();
        for(int i = 0;i < theme.length;i++){
            Map<String,Object> map =new HashMap<>();
            map.put("image",R.drawable.im_pub_no_image);
            map.put("theme",theme[i]);
            map.put("time",time[i]);
            map.put("content",content[i]);
            lists.add(map);
        }
        adapter = new SimpleAdapter(NaviPostActivity.this,lists,R.layout.item_comment
                ,new String[]{"image","theme","time","content"}
                ,new int[]{R.id.headIv,R.id.nameTv,R.id.timeTv,R.id.contentTv});
        listView.setAdapter(adapter);
    }
    public void onClick(View v) {
        //抽象接口的内部方法的实现
        switch (v.getId()) {
            case R.id.snsBtn:
                // 弹出输入法
                if(!ifComment){
                    myComment.setVisibility(View.VISIBLE);
                    myComment_content.requestFocus();
                    ifComment = true;
                }
                else{
                    myComment.setVisibility(View.GONE);
                    ifComment = false;
                }

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.praiseBtn:
                if(!ifPraise){
                    sendPraise();
                    praise_button.setImageResource(R.drawable.im_praise_blue);
                    ifPraise = true;
                    Toast.makeText(getApplicationContext(), "点赞成功！", Toast.LENGTH_SHORT).show();
                }
                else{
                    praise_button.setImageResource(R.drawable.im_praise);
                    ifPraise = false;
                }

        }
    }

    public void sendPraise(){

    }

}