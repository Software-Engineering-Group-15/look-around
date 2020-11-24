

package com.example.lookarounddemo;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpeng.jptabbar.JPTabBar;

import org.jetbrains.annotations.Nullable;
/**
 * Created by jpeng on 16-11-14.
 */
public class MapPager extends Fragment implements View.OnClickListener  {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.pager_map,null);
        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        JPTabBar tabBar = (JPTabBar) ((Activity)getContext()).findViewById(R.id.tabbar);
        tabBar.setTabTypeFace("fonts/Jaden.ttf");
    }
}