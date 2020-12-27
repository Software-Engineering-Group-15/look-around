package com.example.lookarounddemo;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.lookarounddemo.data.MarkersArray;
import com.example.lookarounddemo.data.User;
import com.example.lookarounddemo.data.myMarker;
import com.jpeng.jptabbar.JPTabBar;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * AMapV2地图中介绍使用active deactive进行定位<br>
 * 自定义定位模式，当sdk中提供的定位模式不满足要求时，可以自定义<br>
 *
 * 自定义效果<br>
 * 1、定位成功后， 小蓝点和和地图一起移动到定位点<br>
 * 2、手势操作地图后模式修改为 仅定位不移动到中心点<br>
 *
 *
 */
public class MapPager extends Fragment implements LocationSource,
        AMapLocationListener, AMap.OnMapTouchListener, View.OnClickListener {
    private Activity mActivity;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private MarkersArray mMarkers = new MarkersArray();
    private boolean justPosted = false;
    private double cLa;
    private double cLo;
    boolean useMoveToLocationWithMapMode = true;

    //自定义定位小蓝点的Marker
    Marker locationMarker;

    //坐标和经纬度转换工具
    Projection projection;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("amap","进入onCreateView状态");
        mActivity = getActivity();
        View layout = inflater.inflate(R.layout.pager_map,null);//设置对应的XML布局文件
        mapView = (MapView) layout.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        Bundle bundle = getArguments();
        if(bundle != null){
            justPosted = bundle.getBoolean("just_post");
            Log.i("new", "刚刚新建了一个post");
            Toast.makeText(mActivity.getApplicationContext(), "发送成功！", Toast.LENGTH_SHORT).show();

        }
        init();
        return layout;
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            Log.i("amap","不存在aMap，新建");
            aMap = mapView.getMap();
            setUpMap();
        }
        else{
            Log.i("amap","存在aMap，重建");
            aMap.clear();
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        aMap.setOnMapTouchListener(this);
// 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity(), NaviPostActivity.class);
                String markerID = marker.getTitle();
                int index = Integer.valueOf(markerID);
                myMarker tmpMarker = mMarkers.get(index);
                tmpMarker.Print();
                intent.putExtra("marker", tmpMarker);
                startActivity(intent);
                return true;
            }
        };
// 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i("amap","进入onResume状态");
        mapView.onResume();

        useMoveToLocationWithMapMode = true;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i("amap","进入onPause状态");
        mapView.onPause();
        deactivate();

        useMoveToLocationWithMapMode = false;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("amap","进入onDestroyView状态");
//        aMap.clear();
        //mapView.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("amap","进入onDestroy状态");
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
        if(null != mMarkers){
            mMarkers.clear();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                cLa = amapLocation.getLatitude();
                cLo = amapLocation.getLongitude();
                User.setLa(cLa);
                User.setLo(cLo);
                LatLng latLng = new LatLng(cLa, cLo);
                //展示自定义定位小蓝点
                if(locationMarker == null) {
                    Log.i("amap","首次定位");
                    //首次定位
                    locationMarker = aMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromView(getMyView(0)))
                            .anchor(0.5f, 0.5f).title("MyLocation"));

                    //首次定位,选择移动到地图中心点并修改级别到15级
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else {
                    Log.i("amap","再次定位");
                    if(useMoveToLocationWithMapMode) {
                        //二次以后定位，使用sdk中没有的模式，让地图和小蓝点一起移动到中心点（类似导航锁车时的效果）
                        startMoveLocationAndMap(latLng);
                    } else {
                        startChangeLocation(latLng);
                    }

                }
                Log.i("amap",cLa + "," + cLo);
                mMarkers.ArrayUpdate(cLa, cLo);
                showMarkers();
            }
            else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }


    /**
     * 修改自定义定位小蓝点的位置
     * @param latLng
     */
    private void startChangeLocation(LatLng latLng) {

        if(locationMarker != null) {
            LatLng curLatlng = locationMarker.getPosition();
            if(curLatlng == null || !curLatlng.equals(latLng)) {
                locationMarker.setPosition(latLng);
            }
        }
    }

    //自定义高德标点
    protected View getMyView(int color) {
        View view = null;
        if(color == 0){
            view = getActivity().getLayoutInflater().inflate(R.layout.marker_location, null);
        }
        else if(color == 1){
            view = getActivity().getLayoutInflater().inflate(R.layout.marker_blue, null);
        }
        else if(color == 2){
            view = getActivity().getLayoutInflater().inflate(R.layout.marker_red, null);
        }

        return view;

    }
    /**
     * 同时修改自定义定位小蓝点和地图的位置
     * @param latLng
     */
    private void startMoveLocationAndMap(LatLng latLng) {

        //将小蓝点提取到屏幕上
        if(projection == null) {
            projection = aMap.getProjection();
        }
        if(locationMarker != null && projection != null) {
            LatLng markerLocation = locationMarker.getPosition();
            Point screenPosition = aMap.getProjection().toScreenLocation(markerLocation);
            locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

        }

        //移动地图，移动结束后，将小蓝点放到放到地图上
        myCancelCallback.setTargetLatlng(latLng);
        //动画移动的时间，最好不要比定位间隔长，如果定位间隔2000ms 动画移动时间最好小于2000ms，可以使用1000ms
        //如果超过了，需要在myCancelCallback中进行处理被打断的情况
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng),1000,myCancelCallback);

    }


    MyCancelCallback myCancelCallback = new MyCancelCallback();

    @Override
    public void onTouch(MotionEvent motionEvent) {
        Log.i("amap","onTouch 关闭地图和小蓝点一起移动的模式");
        useMoveToLocationWithMapMode = false;
    }

    /**
     * 监控地图动画移动情况，如果结束或者被打断，都需要执行响应的操作
     */
    class MyCancelCallback implements AMap.CancelableCallback {

        LatLng targetLatlng;
        public void setTargetLatlng(LatLng latlng) {
            this.targetLatlng = latlng;
        }

        @Override
        public void onFinish() {
            if(locationMarker != null && targetLatlng != null) {
                locationMarker.setPosition(targetLatlng);
            }
        }

        @Override
        public void onCancel() {
            if(locationMarker != null && targetLatlng != null) {
                locationMarker.setPosition(targetLatlng);
            }
        }
    };



    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        Log.i("amap","开启定位");
        mListener = listener;
        if (mlocationClient == null) {
            Log.i("amap","新建定位监听");
            mlocationClient = new AMapLocationClient(mActivity);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是指定位间隔
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        Log.i("amap","关闭定位");
        mListener = null;
        locationMarker = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    private void showMarkers() {
//        if(justPosted){
//            //mMarkers.addNewpost(cLa, cLo);
//            justPosted = false;
//        }
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        int size = mMarkers.size();
        boolean ifnew = true;
        for (int j = 0; j < size; j++) {
            myMarker m = mMarkers.get(j);
            int color = 1;
            if(m.getPublisher().equals(User.getName()))
                color = 2;
            Log.i("markers", mapScreenMarkers.size() + "");
            for (int i = 0; i < mapScreenMarkers.size(); i++) {
                Marker marker = mapScreenMarkers.get(i);
                if (marker.getTitle().equals(m.getTitle())) {
                    ifnew = false;
                }
            }
            if(ifnew)
                aMap.addMarker(new MarkerOptions().position(new LatLng(m.getLatitude(), m.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromView(getMyView(color)))
                        .anchor(0.5f, 1.0f)
                        .title(m.getTitle()));
        }
        Log.i("amap","展示标志");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        JPTabBar tabBar = (JPTabBar) ((Activity)getContext()).findViewById(R.id.tabbar);
        tabBar.setTabTypeFace("fonts/Jaden.ttf");
    }

}