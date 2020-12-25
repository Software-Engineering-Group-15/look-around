package com.example.lookarounddemo.data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class myMarker implements Serializable {
    private double latitude;//纬度
    private double longitude;//经度
    private String publisher;
    private String title;
    private String time;
    private String content;
    private boolean ifnew = false;

    private ArrayList<CommentItem> commentsArray = new ArrayList<CommentItem>();

    public myMarker(double latitude, double longitude, String title){
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public myMarker(double latitude, double longitude, String publisher, String title, String time, String content, boolean ifnew){
        this.latitude = latitude;
        this.longitude = longitude;
        this.publisher = publisher;
        this.title = title;
        this.time = time;
        this.content = content;
        this.ifnew = ifnew;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTime() {
        return time;
    }

    public boolean getIfnew() {
        return ifnew;
    }

    public void addCommentItem(CommentItem comment){
        this.commentsArray.add(comment);
    }

    public CommentItem getCommentItem(int index){
        return this.commentsArray.get(index);
    }

    public int getCommentsLen(){
        return this.commentsArray.size();
    }
    public void Print(){
        Log.i("marker", publisher);
        Log.i("marker", time);
        Log.i("marker", title);
        Log.i("marker", content);
    }

}
