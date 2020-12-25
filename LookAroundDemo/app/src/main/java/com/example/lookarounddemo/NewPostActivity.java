package com.example.lookarounddemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.lookarounddemo.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class NewPostActivity extends AppCompatActivity {
    private static final String TAG = "";
    private EditText postText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        postText = (EditText) findViewById(R.id.new_post_txt);

    }

    public void map(View view){
        Intent intent = new Intent(NewPostActivity.this, ControllerActivity.class);
        switch(view.getId()){
            case R.id.new_post_post:
                intent.putExtra("new_post",1);
                intent.putExtra("post_txt",postText.getText().toString());
                break;
            case R.id.new_post_back:
        }
        new Thread(postRun).start();//开启新线程

        startActivity(intent);
    }
    Runnable postRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestPost();
        }
    };

    private void requestPost() {
        //测试http json request
        InputStream is = null;
        String result = "";
        try {
            Log.e("uuu","尝试建立连接");
            String baseUrl = "http://39.98.75.17:80/post";
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
            urlConn.setRequestProperty("Authorization", "Bearer JWT");
            urlConn.connect();
            Log.e("uuu","尝试建立连接3");
            // POST请求
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            JSONObject postOBJ = makePost();
            String json = java.net.URLEncoder.encode(postOBJ.toString(), "utf-8");
            Log.i("json", json);
            out.writeBytes(json);
            out.flush();
            out.close();
            // 判断请求是否成功
            Log.e("uuu","尝试建立连接4");
            int p = urlConn.getResponseCode();
            String pp = urlConn.getResponseMessage();
            Log.e("uuu","尝试建立连接5");
            Log.e("uuu",p+"ooo"+pp);
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String results = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Post方式请求成功，result--->" + results);
                Intent intent = new Intent(this, ControllerActivity.class);
                startActivity(intent);
            } else {
                Log.e(TAG, "Post方式请求失败" + urlConn.getResponseMessage().toString());
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
    public JSONObject makePost(){
        JSONObject postOBJ = new JSONObject();
        try {

            JSONObject textOBJ = new JSONObject();
            textOBJ.put("text", "this is my posts.");//postText.getText().toString());
            postOBJ.put("post", textOBJ);
//            postOBJ.put("postID", Integer.toString((int)(Math.random()*1000)));
//            String myName = User.getName();

//            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0000");
//            String time = sDateFormat.format(new java.util.Date());
//            postOBJ.put("time", time);

//            String location = Double.toString(User.getLa()) + ',' + Double.toString(User.getLo());
            JSONObject locationOBJ = new JSONObject();
            locationOBJ.put("lat", (int)User.getLa());
            locationOBJ.put("long", (int)User.getLo());
            postOBJ.put("location", locationOBJ);

            JSONObject publisherOBJ = new JSONObject();
            publisherOBJ.put("userName", User.getName());
            postOBJ.put("publisher", publisherOBJ);
//            //构建评论
//            JSONObject commentListObj = new JSONObject();
//            commentListObj.put("totalCnt", 0);
//            JSONArray commentsArr = new JSONArray();
//            commentListObj.put("comments", commentsArr);
//            postOBJ.put("CommentList", commentListObj);
//            //构建点赞
//            postOBJ.put("favorCnt", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json", postOBJ.toString());
        return postOBJ;
    }
}