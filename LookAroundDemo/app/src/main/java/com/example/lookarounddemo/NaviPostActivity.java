package com.example.lookarounddemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
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
import com.example.lookarounddemo.data.User;
import com.example.lookarounddemo.data.myMarker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private String postID;
    private static final String TAG = "";

    private TextView post_publisher;
    private TextView post_content;
    private TextView post_time;

    private ImageView praise_button;
    private ImageView comment_button;
    private boolean ifPraise = false;
    private LinearLayout myComment;
    private boolean ifComment = false;
    private EditText myComment_content;
    private Button myCommentSend;
    private String commentText;

    private int[] imageViews = {R.drawable.im_pub_no_image,R.drawable.im_pub_no_image,R.drawable.im_pub_no_image};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_post);
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
        myCommentSend = findViewById(R.id.comment_send);
        myCommentSend.setOnClickListener(this);

//        准备数据源
        myMarker tmpMarker = (myMarker) getIntent().getSerializableExtra("marker");
        post_publisher.setText(tmpMarker.getPublisher());
        post_time.setText(tmpMarker.getTime());
        post_content.setText(tmpMarker.getContent());
        postID = tmpMarker.getPostID();

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

    public void map(View view){
        Intent intent = new Intent(NaviPostActivity.this, ControllerActivity.class);
//        switch(view.getId()){
//            case R.id.new_post_post:
//                intent.putExtra("new_post",1);
//                intent.putExtra("post_txt",postText.getText().toString());
//                break;
//            case R.id.new_post_back:
//        }
//        new Thread(postRun).start();//开启新线程
        startActivity(intent);
    }

    public void onClick(View v) {
        //抽象接口的内部方法的实现
        switch (v.getId()) {
            case R.id.snsBtn:
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                if(!ifComment){
                    myComment.setVisibility(View.VISIBLE);
                    myComment_content.requestFocus();
                    ifComment = true;
                }
                else{
                    myComment.setVisibility(View.GONE);
                    ifComment = false;
                }
                Log.i("comment", "开启了评论");
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
                break;
            case R.id.comment_send:
                Log.i("comment", "发送了评论");
                commentText = myComment_content.getText().toString();
                Toast.makeText(getApplicationContext(), commentText, Toast.LENGTH_SHORT).show();
                sendComment();
                myComment_content.setText("");
                myComment.setVisibility(View.GONE);
                ifComment = false;
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(myComment_content.getWindowToken(), 0);

                break;
        }
    }

    public void sendPraise(){

    }

    public void sendComment(){
        new Thread(postRun).start();//开启新线程
    }
    public void refreshComment() {new Thread(postRun2).start();}

    Runnable postRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            commentPost();
        }
    };

    Runnable postRun2 = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            commentRefresh();
        }
    };

    private void commentPost() {
        //测试http json request
        InputStream is = null;
        String result = "";
        try {
            Log.e("uuu","尝试建立连接");
            String baseUrl = "http://39.98.75.17:80/post/comment";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            Log.e("uuu","尝试建立连接2");
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            Log.e("uuu","尝试建立连接2.5");
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            Log.e("uuu","尝试建立连接2.7");
            urlConn.setInstanceFollowRedirects(true);
            Log.e("uuu","尝试建立连接2.8");
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.connect();
            Log.e("uuu","尝试建立连接3");
            // POST请求
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            JSONObject postOBJ = makeComment();
            String json = postOBJ.toString();
            Log.i("json", json);
            out.writeBytes(json);
            out.flush();
            out.close();
            // 判断请求是否成功
            Log.e("uuu","尝试建立连接4");
            int p = urlConn.getResponseCode();
            String pp = urlConn.getResponseMessage();
            Log.e("uuu","尝试建立连接5");
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String results = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Post方式请求成功，result--->" + results);
                new Thread(postRun2).start();//开启新线程 刷新评论列表

            } else {
                Log.e(TAG, "Post方式请求失败" + urlConn.getResponseMessage().toString());
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private void commentRefresh() {
        try {
            String baseUrl = "http://39.98.75.17:80/post/getmessage/" + postID;
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                JSONObject json_result = new JSONObject(result);
                Log.e("111", "Get方式请求成功，result--->" + result);
                JSONObject dataObject = json_result.getJSONObject("data");
                JSONObject postObject = dataObject.getJSONObject("post");
                JSONArray commentsArray = postObject.getJSONArray("commentList");
                lists.clear();
                int commentsLen = commentsArray.length();
                for(int j=0;j<commentsLen;j++){
                    JSONObject commentObject = commentsArray.getJSONObject(j);
                    String commentID = commentObject.getString("commentID");
                    String commentTime = timeString(commentObject.getString("time"));
                    String commentPublisher = commentObject.getString("publisher");
                    String commentText = commentObject.getString("text");

                    Map<String,Object> map =new HashMap<>();
                    map.put("image",R.drawable.im_pub_no_image);
                    map.put("publisher",commentPublisher);
                    map.put("time",commentTime);
                    map.put("content", commentText);
                    lists.add(map);
                }
                adapter  = null;
                adapter = new SimpleAdapter(NaviPostActivity.this,lists,R.layout.item_comment
                        ,new String[]{"image","publisher","time","content"}
                        ,new int[]{R.id.headIv,R.id.nameTv,R.id.timeTv,R.id.contentTv});
                listView.setAdapter(adapter);

            } else {
                Log.e("111", "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("111", e.toString());
        }
    }


    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }


    public JSONObject makeComment(){
        JSONObject commentOBJ = new JSONObject();
        try {
            commentOBJ.put("text", commentText);//postText.getText().toString());
            commentOBJ.put("postID", postID);
            commentOBJ.put("userName", User.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json", commentOBJ.toString());
        return commentOBJ;
    }

    public String timeString(String time){
        String[] timeArr = time.split("T");
        String[] dateArr = timeArr[0].split("-");
        String[] clockArr = timeArr[1].split("\\.");
        String final_time = dateArr[0] + '年' + dateArr[1] + '月' + dateArr[2] + '日' + clockArr[0];
        return final_time;
    }
}