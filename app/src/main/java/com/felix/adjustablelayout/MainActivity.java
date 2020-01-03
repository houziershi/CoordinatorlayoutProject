package com.felix.adjustablelayout;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MaiActivity";

    private static final String APPBAR_LAYOUT_TAG = "fuli_page_hide_tag";

    //fragment的适配器
    private MainTabFragmentAdapter mainTabFragmentAdapter;

    //viewpager
    private ViewPager mViewPager;

    //AppBarLayout
    private AppBarLayout mAppBarLayout;

    //顶部HeaderLayout
    private LinearLayout headerLayout;

    private SmartTabLayout mTabs;

    //是否隐藏了头部
    private boolean isHideHeaderLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //初始化方法
    private void init() {
        mTabs = (SmartTabLayout) findViewById(R.id.tabs);
        mainTabFragmentAdapter = new MainTabFragmentAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mainTabFragmentAdapter);
        mTabs.setViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(mainTabFragmentAdapter.getCount());
        headerLayout = (LinearLayout) findViewById(R.id.ll_header_layout);
        initAppBarLayout();
    }

    // 初始化AppBarLayout
    private void initAppBarLayout() {
        LayoutTransition mTransition = new LayoutTransition();
        /**
         * 添加View时过渡动画效果
         */
        ObjectAnimator addAnimator = ObjectAnimator.ofFloat(null, "translationY", 0, 1.f).
                setDuration(mTransition.getDuration(LayoutTransition.APPEARING));
        mTransition.setAnimator(LayoutTransition.APPEARING, addAnimator);

        //header layout height
//        final int headerHeight = getResources().getDimensionPixelOffset(R.dimen.header_height);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAppBarLayout.setLayoutTransition(mTransition);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i(TAG, " <onOffsetChanged>..........." + verticalOffset);
                verticalOffset = Math.abs(verticalOffset);
                if (verticalOffset >= headerLayout.getBottom()) {
                    //当偏移量超过顶部layout的高度时，我们认为他已经完全移动出屏幕了
                    hideHead();
                }
            }
        });
    }

    private void hideHead() {
        Toast.makeText(this, "隐藏头部", Toast.LENGTH_LONG);
        isHideHeaderLayout = true;
        /**Created by guokun on 2020/1/3.
         * Description: 核心代码*/
        mAppBarLayout.setTag(APPBAR_LAYOUT_TAG);
    }

    private void showHead() {
        Toast.makeText(this, "显示头部", Toast.LENGTH_LONG);
        isHideHeaderLayout = false;
        /**Created by guokun on 2020/1/3.
         * Description: 核心代码*/
        mAppBarLayout.setTag(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //监听返回键
            if (isHideHeaderLayout) {
                showHead();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
