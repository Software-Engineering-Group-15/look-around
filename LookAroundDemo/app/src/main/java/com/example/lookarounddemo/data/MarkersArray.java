package com.example.lookarounddemo.data;

import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MarkersArray extends ArrayList<myMarker> {
    double mla;
    double mlo;
    public myMarker getFirstMarker(){
        if (this.isEmpty()){
            //Log.i("MarkersArray","没有元素");
            return null;
        }
        //Log.i("MarkersArray","有个元素");
        return this.get(0);
    }

    public void ArrayUpdate(double la, double lo){
        this.mla = la;
        this.mlo = lo;
        postGET(mla,mlo);
//        if (this.isEmpty()){
//            for(int i=0; i<3; i++){
//                this.add(new myMarker(la + Math.random()/100,lo + Math.random()/100, "更新的标记"));
//            }
//            for(int i=0; i<3; i++){
//                this.add(new myMarker(la + Math.random()/100,lo - Math.random()/100, "更新的标记"));
//            }
//            for(int i=0; i<3; i++){
//                this.add(new myMarker(la - Math.random()/100,lo - Math.random()/100, "更新的标记"));
//            }
//            for(int i=0; i<3; i++){
//                this.add(new myMarker(la - Math.random()/100,lo + Math.random()/100, "更新的标记"));
//            }
//        }
    }

    public void postGET(double la, double lo){
        new Thread(getRun).start();
    }

    /**
     * get请求线程
     */
    Runnable getRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestGet(mla, mlo);
        }
    };
    private void requestGet(double la, double lo) {
        try {
            String baseUrl = "http://39.98.75.17:80/posts";
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
                JSONArray postsArray = dataObject.getJSONArray("posts");
                int postsLen = postsArray.length();
                for(int i=0;i<postsLen;i++){
                    JSONObject postObject = postsArray.getJSONObject(i);
                    String postID = postObject.getString("id");
                    String time = timeString(postObject.getString("time"));
                    String publisher = postObject.getString("publisher");
                    String location = postObject.getString("location");
                    String txt = postObject.getString("text");
                    //处理location格式
                    String[] locationArr = location.split(",");
                    double tla = Double.valueOf(locationArr[0]);
                    double tlo = Double.valueOf(locationArr[1]);

                    myMarker newMarker = new myMarker(tla, tlo, publisher, Integer.toString(i), time, txt, false);
                    Log.i("post", postID);
                    Log.i("post", publisher);
                    Log.i("post", location);
                    Log.i("post", time);
                    Log.i("post", txt);
                    JSONArray commentsArray = postObject.getJSONArray("commentList");
                    int commentsLen = commentsArray.length();
                    for(int j=0;j<commentsLen;j++){
                        JSONObject commentObject = commentsArray.getJSONObject(j);
                        String commentID = commentObject.getString("commentID");
                        String commentTime = timeString(commentObject.getString("time"));
                        String commentPublisher = commentObject.getString("publisher");
                        String commentText = commentObject.getString("text");
                        Log.i("comment", commentID);
                        Log.i("comment", commentPublisher);
                        Log.i("comment", commentTime);
                        Log.i("comment", commentText);
                        newMarker.addCommentItem(new CommentItem(commentID, commentPublisher, commentTime, commentText));
                    }
                    this.add(newMarker);
                }

            } else {
                Log.e("111", "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("111", e.toString());
        }
    }

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
            Log.e("111", e.toString());
            return null;
        }
    }

    public String timeString(String time){
        String[] timeArr = time.split("T");
        String[] dateArr = timeArr[0].split("-");
        String[] clockArr = timeArr[1].split("\\.");
        String final_time = dateArr[0] + '年' + dateArr[1] + '月' + dateArr[2] + '日' + clockArr[0];
        return final_time;
    }

    public void addNewpost(double la, double lo){
        this.add(new myMarker(la, lo, "我", "刚发的post","20.30.10", "暂无内容", true));
    }
}
