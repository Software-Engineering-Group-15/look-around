package com.example.lookarounddemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lookarounddemo.data.User;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "";
    public static String username = "";
    public static int flag = 0;
    private boolean ifsuccess = true;
    private boolean ifsuccess2 = false;
    private boolean ifServer = true;
    private EditText reg_input_useName;
    private EditText reg_input_phonenum;
    private EditText reg_input_password;
    private EditText reg_input_password_again;
    private TextView reg_failed1;
    private TextView reg_failed2;
    private TextView reg_failed3;
    private TextView reg_failed4;
    private TextView reg_failed5;
    private HashMap<String, String> stringHashMap;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        reg_input_useName = (EditText) findViewById(R.id.reg_input_username);
        //reg_input_phonenum = (EditText) findViewById(R.id.reg_input_phonenum);
        reg_input_password = (EditText) findViewById(R.id.reg_input_password);
        reg_input_password_again = (EditText) findViewById(R.id.reg_input_password_again);
        reg_failed1 = (TextView) findViewById(R.id.reg_failed3);
        reg_failed2 = (TextView) findViewById(R.id.reg_failed5);
        reg_failed3 = (TextView) findViewById(R.id.reg_failed6);
        reg_failed4 = (TextView) findViewById(R.id.reg_failed7);
        reg_failed5 = (TextView) findViewById(R.id.reg_failed8);
//        mHandler = new Handler();//创建Handler
        stringHashMap = new HashMap<>();
    }

    public void login(View view){
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        stringHashMap.put("password", reg_input_password.getText().toString());
        stringHashMap.put("username", reg_input_useName.getText().toString());
        username = reg_input_useName.getText().toString();
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
        if(!reg_input_useName.getText().toString().equals("") && !reg_input_password.getText().toString().equals("") && reg_input_password.getText().toString().equals(reg_input_password_again.getText().toString())){
            try {
                ifsuccess = true;
                ifsuccess2 = false;
                Thread thread = new Thread(postRun);
                thread.start();//开启新线程
                thread.join(2000);
                while(ifsuccess){
                    if(ifsuccess2)
                        break;
                }
                if(!ifsuccess2) {
                    reg_failed1.setVisibility(View.VISIBLE);
                    reg_failed3.setVisibility(View.GONE);
                    reg_failed4.setVisibility(View.GONE);
                    reg_failed2.setVisibility(View.VISIBLE);
                    reg_failed5.setVisibility(View.GONE);
                }
                if(!ifServer){
                    reg_failed1.setVisibility(View.VISIBLE);
                    reg_failed3.setVisibility(View.GONE);
                    reg_failed4.setVisibility(View.GONE);
                    reg_failed2.setVisibility(View.GONE);
                    reg_failed5.setVisibility(View.VISIBLE);
                }
            } catch (InterruptedException e) {
                reg_failed1.setVisibility(View.VISIBLE);
                reg_failed3.setVisibility(View.GONE);
                reg_failed4.setVisibility(View.GONE);
                reg_failed2.setVisibility(View.GONE);
                reg_failed5.setVisibility(View.VISIBLE);
            }
        }
        else{
            if(reg_input_useName.getText().toString().equals("") || reg_input_password.getText().toString().equals("")){
                reg_failed1.setVisibility(View.VISIBLE);
                reg_failed2.setVisibility(View.GONE);
                reg_failed4.setVisibility(View.GONE);
                reg_failed3.setVisibility(View.VISIBLE);
                reg_failed5.setVisibility(View.GONE);
            }
            else{
                reg_failed1.setVisibility(View.VISIBLE);
                reg_failed2.setVisibility(View.GONE);
                reg_failed3.setVisibility(View.GONE);
                reg_failed4.setVisibility(View.VISIBLE);
                reg_failed5.setVisibility(View.GONE);
            }
        }

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

            String baseUrl = "http://39.98.75.17/register";
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
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.accumulate("username",paramsMap.get("username"));
            obj.accumulate("password",paramsMap.get("password"));
            String json = java.net.URLEncoder.encode(obj.toString(), "utf-8");
            out.writeBytes(obj.toString());
            out.flush();
            out.close();

            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200 && urlConn.getHeaderField("Authorization")!=null) {
                // 获取返回的数据
                flag = 1;
                ifsuccess = true;
                ifsuccess2 = true;
                ifServer = true;
                String t = urlConn.getHeaderField("Authorization");
                Log.e(TAG, "Post方式请求成功，Authorization--->" + t);
                String results = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Post方式请求成功，result--->" + results);
                User.setName(reg_input_useName.getText().toString());
                Intent intent = new Intent(this, ControllerActivity.class);
                startActivity(intent);
            } else {
                ifsuccess = false;
                ifsuccess2 = false;
                ifServer = true;
                Log.e(TAG, "Post方式请求失败");
                /* 创建AlertDialog对象并显示 */

            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            ifsuccess = false;
            ifServer = false;
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