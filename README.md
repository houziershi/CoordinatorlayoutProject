# 账号页仿新浪微博发现页效果
##### 需求
账号页和福利页两个页面合在一起滑动，交互类似发现页（微博发现页和懒人听书发现页）。
1. 账号页是头部，账号页滑出页面后，只有点击（返回或退出）才可以滑出账号页。
2. AppBarLayout和CoordinatorLayout协调使用


##### 设计 [demo](https://github.com/houziershi/CoordinatorlayoutProject)

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.felix.adjustablelayout.MainActivity">

    <android.support.design.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/transparent"
        android:orientation="vertical">

        <android.support.design.widget.AppBarLayout
            android:id="@+id/appbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/colorPrimaryDark"
            android:theme="@style/MainAppBar"
            app:layout_behavior=".FixAppBarLayoutBehavior">

            <LinearLayout
                android:id="@+id/ll_header_layout"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="@color/colorPrimaryDark"
                android:orientation="vertical"
                app:layout_scrollFlags="scroll|exitUntilCollapsed">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="40dp"
                    android:gravity="center"
                    android:text="头部" />

            </LinearLayout>

            <com.ogaclejapan.smarttablayout.SmartTabLayout
                android:id="@+id/tabs"
                android:layout_width="match_parent"
                android:layout_height="@dimen/tab_height"
                android:background="@android:color/white"
                app:layout_scrollFlags="scroll"
                app:stl_customTabTextLayoutId="@layout/custom_tab"
                app:stl_customTabTextViewId="@+id/custom_text"
                app:stl_distributeEvenly="true"
                app:stl_dividerColor="@color/colorPrimary"
                app:stl_dividerThickness="0dp"
                app:stl_indicatorColor="@color/colorPrimary"
                app:stl_indicatorCornerRadius="0dp"
                app:stl_indicatorGravity="bottom"
                app:stl_indicatorInterpolation="linear"
                app:stl_indicatorThickness="2.5dp"
                app:stl_indicatorWithoutPadding="true"
                app:stl_underlineColor="@android:color/transparent"
                app:stl_underlineThickness="0dp" />

        </android.support.design.widget.AppBarLayout>

        <android.support.v4.view.ViewPager
            android:id="@+id/viewpager"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_behavior="@string/appbar_scrolling_view_behavior" />

    </android.support.design.widget.CoordinatorLayout>

</RelativeLayout>

```


```
package com.felix.adjustablelayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import java.lang.reflect.Field;

/**
 * 解决appbarLayout若干问题：
 * （1）快速滑动appbarLayout会出现回弹
 * （2）快速滑动appbarLayout到折叠状态下，立马下滑，会出现抖动的问题
 * （3）滑动appbarLayout，无法通过手指按下让其停止滑动
 *
 * @author yuruiyin
 * @version 2018/1/3
 */
public class FixAppBarLayoutBehavior extends AppBarLayout.Behavior {

    private static final String TAG_NESTED_SCROLL = "tag_FL_nested_scroll";

    private static final int TYPE_FLING = 1;

    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    private AppBarLayout appBarLayout;
    private CoordinatorLayout coordinatorLayout;

    public FixAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean setTopAndBottomOffset(int offset) {
        if (appBarLayout != null && coordinatorLayout != null) {
            if (appBarLayout.getTag() != null) {
                /**Created by guokun on 2020/1/3.
                 * Description: 核心代码*/
                offset = -(appBarLayout.getChildAt(0).getBottom());
            } /*else {
                View childNestedScrollView = coordinatorLayout.findViewWithTag(TAG_NESTED_SCROLL);
                if (childNestedScrollView != null && childNestedScrollView.getVisibility() == View.GONE) {

                    Rect rect = new Rect();
                    appBarLayout.getLocalVisibleRect(rect);

                    int childHeight = appBarLayout.getHeight();
                    int parentHeight = coordinatorLayout.getHeight();
                    //parentHeight 一般是match_parent;
                    if (childHeight <= parentHeight && rect.bottom <= parentHeight) {
                        //appBarLayout 高度低于 coordinatorLayout
                        offset = 0;
                    }
                    if (childHeight > parentHeight && Math.abs(offset) >= childHeight - parentHeight) {
                        //appBarLayout 高度 大于 coordinatorLayout，且offset大于高度差(即appBarLayout底部可见时的偏移量)
                        offset = parentHeight - childHeight;
                    }
                }
            }*/
        }

        return super.setTopAndBottomOffset(offset);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        coordinatorLayout = parent;
        appBarLayout = child;
        shouldBlockNestedScroll = false;
        if (isFlinging) {
            shouldBlockNestedScroll = true;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                stopAppbarLayoutFling(child);  //手指触摸屏幕的时候停止fling事件
                break;
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
     *
     * @return Field
     */
    private Field getFlingRunnableField() throws NoSuchFieldException {
        try {
            // support design 27及以下版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("mFlingRunnable");
        } catch (NoSuchFieldException e) {
            // 可能是28及以上版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("flingRunnable");
        }
    }

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     *
     * @return Field
     */
    private Field getScrollerField() throws NoSuchFieldException {
        try {
            // support design 27及以下版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("mScroller");
        } catch (NoSuchFieldException e) {
            // 可能是28及以上版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("scroller");
        }
    }

    /**
     * 停止appbarLayout的fling事件
     *
     * @param appBarLayout
     */
    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            Field flingRunnableField = getFlingRunnableField();
            Field scrollerField = getScrollerField();
            flingRunnableField.setAccessible(true);
            scrollerField.setAccessible(true);

            Runnable flingRunnable = (Runnable) flingRunnableField.get(this);
            OverScroller overScroller = (OverScroller) scrollerField.get(this);
            if (flingRunnable != null) {
                appBarLayout.removeCallbacks(flingRunnable);
                flingRunnableField.set(this, null);
            }
            if (overScroller != null && !overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        stopAppbarLayoutFling(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {

        //type返回1时，表示当前target处于非touch的滑动，
        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动子类还未结束其自身的fling
        //所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        if (type == TYPE_FLING) {
            isFlinging = true;
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int
            dxUnconsumed, int dyUnconsumed, int type) {
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }

}

```

```
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
        Toast.makeText(this, "隐藏头部", Toast.LENGTH_SHORT).show();
        isHideHeaderLayout = true;
        /**Created by guokun on 2020/1/3.
         * Description: 核心代码*/
        mAppBarLayout.setTag(APPBAR_LAYOUT_TAG);
    }

    private void showHead() {
        Toast.makeText(this, "显示头部", Toast.LENGTH_SHORT).show();
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

```

##### 关键代码

```
    //AppBarLayout滑出屏幕
    if (appBarLayout.getTag() != null) {
        /**Created by guokun on 2020/1/3.
         * Description: 核心代码*/
        offset = -(appBarLayout.getChildAt(0).getBottom());
    }
```
当头部滑出页面后，AppBarLayout的TopAndBottomOffset始终设为头部高度，这样一来就无法滑出头部AppBarLayout的内容；系统退出时，AppBarLayout恢复正常使用；


##### 参考
- [Android - 实现微博发现页面的效果（利用CoordinatorLayout+AppBarLayout）](https://www.jianshu.com/p/18363b0ea60f)

