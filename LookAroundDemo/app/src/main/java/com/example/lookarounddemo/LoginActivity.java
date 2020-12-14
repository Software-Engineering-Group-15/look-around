package com.example.lookarounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText log_input_phonenum;
    private EditText log_input_password;
    private HashMap<String, String> stringHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_input_phonenum = (EditText) findViewById(R.id.log_input_phonenum);
        log_input_password = (EditText) findViewById(R.id.log_input_password);
        stringHashMap = new HashMap<>();
    }

    public void login_finish(View view){
        stringHashMap.put("username", log_input_phonenum.getText().toString());
        stringHashMap.put("password", log_input_password.getText().toString());
        new Thread(postRun).start();//开启新线程
        Intent intent = new Intent(this, ControllerActivity.class);
        startActivity(intent);
    }

    public void forget_password(View view){
        Intent intent = new Intent(this, FindPasswordActivity.class);
        startActivity(intent);
    }

    Runnable postRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestPost(stringHashMap);
        }
    };

    private void requestPost(HashMap<String, String> paramsMap) {
//测试http json request
        InputStream is = null;
        String result = "";
        try {
            Log.i("login","开始Post");
            String baseUrl = "http://115.27.199.59:8080/user/login";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
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
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.connect();
            // POST请求
            DataOutputStream out = new
                    DataOutputStream(urlConn.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.accumulate("username","bbb");
            obj.accumulate("password","213");
            String json = java.net.URLEncoder.encode(obj.toString(), "utf-8");
            out.writeBytes(json);
            out.flush();
            out.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String tmpresult = streamToString(urlConn.getInputStream());
                Log.i("login","post成宫");
            } else {
                Log.i("login","post失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("login",e.toString());
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
            Log.i("login","失败");
            return null;
        }
    }
}