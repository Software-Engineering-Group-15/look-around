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
import android.widget.TextView;
import android.widget.Toast;

import com.example.lookarounddemo.data.CommentItem;
import com.example.lookarounddemo.data.myMarker;

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

    private TextView post_publisher;
    private TextView post_content;
    private TextView post_time;

    private ImageView praise_button;
    private ImageView comment_button;
    private boolean ifPraise = false;
    private LinearLayout myComment;
    private boolean ifComment = false;
    private EditText myComment_content;

    private int[] imageViews = {R.drawable.im_pub_no_image,R.drawable.im_pub_no_image,R.drawable.im_pub_no_image};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //原po相关
        post_publisher = findViewById(R.id.nameTv);
        post_time = findViewById(R.id.timeTv);
        post_content = findViewById(R.id.contentTv);
        //按钮相关
        comment_button = findViewById(R.id.snsBtn);
        praise_button = findViewById(R.id.praiseBtn);
        comment_button.setOnClickListener(this);
        praise_button.setOnClickListener(this);
        //评论相关
        myComment = findViewById(R.id.myComment);
        myComment_content = findViewById(R.id.comment_content);
        listView = (ListView) findViewById(R.id.commentList);
//        准备数据源
        myMarker tmpMarker = (myMarker) getIntent().getSerializableExtra("marker");
        post_publisher.setText(tmpMarker.getPublisher());
        post_time.setText(tmpMarker.getTime());
        post_content.setText(tmpMarker.getContent());

        int commentLen = tmpMarker.getCommentsLen();
        lists = new ArrayList<>();
        for(int i = 0;i < commentLen; i++){
            CommentItem tmpComment = tmpMarker.getCommentItem(i);
            Map<String,Object> map =new HashMap<>();
            map.put("image",R.drawable.im_pub_no_image);
            map.put("publisher",tmpComment.getPublisher());
            map.put("time",tmpComment.getTime());
            map.put("content", tmpComment.getText());
            lists.add(map);
        }
        adapter = new SimpleAdapter(NaviPostActivity.this,lists,R.layout.item_comment
                ,new String[]{"image","publisher","time","content"}
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