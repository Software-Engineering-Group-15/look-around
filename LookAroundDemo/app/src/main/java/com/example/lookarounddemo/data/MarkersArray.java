package com.example.lookarounddemo.data;

import android.util.Log;

import java.util.ArrayList;

public class MarkersArray extends ArrayList<myMarker> {
    public myMarker getFirstMarker(){
        if (this.isEmpty()){
            //Log.i("MarkersArray","没有元素");
            return null;
        }
        //Log.i("MarkersArray","有个元素");
        return this.get(0);
    }

    public void ArrayUpdate(double la, double lo){
        if (this.isEmpty()){
            for(int i=0; i<3; i++){
                this.add(new myMarker(la + Math.random()/100,lo + Math.random()/100, "更新的标记"));
            }
            for(int i=0; i<3; i++){
                this.add(new myMarker(la + Math.random()/100,lo - Math.random()/100, "更新的标记"));
            }
            for(int i=0; i<3; i++){
                this.add(new myMarker(la - Math.random()/100,lo - Math.random()/100, "更新的标记"));
            }
            for(int i=0; i<3; i++){
                this.add(new myMarker(la - Math.random()/100,lo + Math.random()/100, "更新的标记"));
            }
        }
    }

    public void addNewpost(double la, double lo){
        this.add(new myMarker(la, lo, "刚发的post", true));
    }
}
