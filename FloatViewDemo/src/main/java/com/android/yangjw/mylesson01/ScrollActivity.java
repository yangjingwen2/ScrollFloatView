package com.android.yangjw.mylesson01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by yangjw.
 * net:androidxx.cn
 * android课堂笔记
 */
public class ScrollActivity extends AppCompatActivity implements CustomScrollView.IScrollChangedListener{

    private CustomScrollView mScrollView;
    private TextView mInvisibleView; //处于屏幕顶部，默认隐藏
    private TextView mFloatView; //处于ScrollView中，是目标控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        mScrollView = (CustomScrollView) findViewById(R.id.scroll_View);
        mFloatView = (TextView) findViewById(R.id.float_view);
        mInvisibleView = (TextView) findViewById(R.id.invisible_view);
        mScrollView.setScrollChangedListener(this);
    }


    /**
     * 滚动的监听事件方法
     * @param l 当前横向滚动的X轴坐标（表示从l坐标处开始滚动）
     * @param t 当前垂直滚动的Y轴坐标（表示从t坐标处开始滚动）
     * @param oldl 前一次横向滚动的X轴坐标（表示上一次是从oldl坐标处开始滚动）
     * @param oldt 前一次当前垂直滚动的Y轴坐标（表示上一次是从oldt坐标处开始滚动）
     */
    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        //获得目标控件的Y轴坐标
        float targetViewY = mFloatView.getY();

        //判断目标控件是否到达屏幕顶端
        //判断原理：
        //当开始滚动的Y坐标oldt正好是目标控件的Y坐标，表示目标控件位置屏幕顶部
        if (oldt+10 >= targetViewY) {
            if (mInvisibleView.getVisibility() == View.GONE) {
                mInvisibleView.setText("Item04");
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
                mInvisibleView.startAnimation(animation);
                mInvisibleView.setVisibility(View.VISIBLE);
            }
        } else {
            //否则隐藏
            if (mInvisibleView.getVisibility() == View.VISIBLE) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
                mInvisibleView.startAnimation(animation);
                mInvisibleView.setVisibility(View.GONE);
            }
        }
    }
}
