package com.example.lookarounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.lookarounddemo.widget.ItemGroup;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import java.util.ArrayList;
import java.util.List;

public class ControllerActivity extends AppCompatActivity {

//    @Titles
//    private static final String[] mTitles = {"地图","搜索","收藏","用户"};
    @Titles
    private static final String[] mTitles = {"地图","用户"};
//    @SeleIcons
//    private static final int[] mSeleIcons = {R.mipmap.tab1_selected,R.mipmap.tab2_selected,R.mipmap.tab3_selected,R.mipmap.tab4_selected};
    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.tab1_selected,R.mipmap.tab4_selected};
//    @NorIcons
//    private static final int[] mNormalIcons = {R.mipmap.tab1_normal, R.mipmap.tab2_normal, R.mipmap.tab3_normal, R.mipmap.tab4_normal};
    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.tab1_normal, R.mipmap.tab4_normal};
    private List<Fragment> list = new ArrayList<>();

    private NoScrollViewPager mPager;

    private JPTabBar mTabbar;

    private MapPager mTab1;

    private SearchPager mTab2;

    private FavouritePager mTab3;

    private UserPager mTab4;

    ViewPager viewPager;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Intent intent = getIntent();
        int posted = intent.getIntExtra("new_post", 0);

        mTabbar = (JPTabBar) findViewById(R.id.tabbar);
        mPager = (NoScrollViewPager) findViewById(R.id.vp_main);
        mTab1 = new MapPager();
        if(posted == 1){
            Bundle bundle = new Bundle();
            bundle.putBoolean("just_post", true);
            mTab1.setArguments(bundle);
        }
//        mTab2 = new SearchPager();
//        mTab3 = new FavouritePager();
        mTab4 = new UserPager();
        View middleView = mTabbar.getMiddleView();
        MiddleClickListener myMidlistener = new MiddleClickListener();
        middleView.setOnClickListener(myMidlistener);
        mTabbar.setGradientEnable(true);
        mTabbar.setPageAnimateEnable(true);
        list.add(mTab1);
//        list.add(mTab2);
//        list.add(mTab3);
        list.add(mTab4);
        adapter = new Adapter(getSupportFragmentManager(),list);
        mPager.setAdapter(adapter);
        mTabbar.setContainer(mPager);

    }
    public void setCurrentItem(int item) {
        mPager.setCurrentItem(item,true);
        //ItemGroup name = (ItemGroup)mPager.findViewById(R.id.ig_name);
        //name.getContentEdt().setText();
    }
    public void edit_name(String name){
        UserPager u = (UserPager) adapter.getItem(1);
        ItemGroup it_name = (ItemGroup) u.getView().findViewById(R.id.ig_name);
        it_name.getContentEdt().setText(name);
    }

    class MiddleClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ControllerActivity.this, NewPostActivity.class);
            startActivity(intent);
        }
    }
}