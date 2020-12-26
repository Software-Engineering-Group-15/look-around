package com.example.lookarounddemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "";
    private EditText log_input_phonenum;
    private EditText log_input_password;
    private HashMap<String, String> stringHashMap;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_input_phonenum = (EditText) findViewById(R.id.log_input_phonenum);
        log_input_password = (EditText) findViewById(R.id.log_input_password);
//        mHandler = new Handler();//创建Handler
        stringHashMap = new HashMap<>();
    }

    public void login_finish(View view){
        stringHashMap.put("username", log_input_phonenum.getText().toString());
        stringHashMap.put("password", log_input_password.getText().toString());

        new Thread(postRun).start();//开启新线程

//         new Thread() {
//            public void run() {
//                mHandler.post(postRun);
//            }
//        }.start();


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
            Log.e("uuu","尝试建立连接");
            String baseUrl = "http://39.98.75.17/login";
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
            JSONObject obj = new JSONObject();
            obj.accumulate("username",paramsMap.get("username"));
            obj.accumulate("password",paramsMap.get("password"));
            String json = java.net.URLEncoder.encode(obj.toString(),"GBK");
            out.writeBytes(obj.toString());
            out.flush();
            out.close();
            // 判断请求是否成功
            Log.e("uuu","尝试建立连接4");
            int p = urlConn.getResponseCode();
            Log.e("uuu","尝试建立连接5");
            Log.e("uuu",p+"ooo");
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                Map<String, List<String>> results = urlConn.getHeaderFields();
                System.out.println("results:" + results);
                //Log.e(TAG, "Post方式请求成功，result--->" + results);
                Intent intent = new Intent(this, ControllerActivity.class);
                startActivity(intent);
            } else {
                Log.e(TAG, "Post方式请求失败");
                // 创建AlertDialog对象并显示
                /*final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.show();
                // 添加对话框自定义布局
                alertDialog.setContentView(R.layout.login_pop);
                // 获取对话框窗口
                Window window = alertDialog.getWindow();
                // 设置显示窗口的宽高
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                // 设置窗口显示位置
                window.setGravity(Gravity.CENTER);
                // 通过window找布局里的控件
                window.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("log", "进入onclick函数体内");
                        // 隐藏对话框
                        alertDialog.dismiss();
                        //自己进行其他的处理
                    }
                });*/
                Toast.makeText(getApplicationContext(), "登陆失败！请核对信息！", Toast.LENGTH_SHORT).show();
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
}