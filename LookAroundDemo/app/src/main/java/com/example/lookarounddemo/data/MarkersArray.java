package com.example.lookarounddemo.data;

import android.util.Log;

import java.util.ArrayList;

public class MarkersArray extends ArrayList<myMarker> {
    public myMarker getFirstMarker(){
        if (this.isEmpty()){
            Log.i("MarkersArray","没有元素");
            return null;
        }
        Log.i("MarkersArray","有个元素");
        return this.get(0);
    }

    public void ArrayUpdate(){
        if (this.isEmpty()){
            this.add(new myMarker(39.986919 + Math.random()/10,116.354369 + Math.random()/10));
        }
    }
}
