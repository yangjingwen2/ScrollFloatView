package com.android.yangjw.mylesson01;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by yangjw on 2016/4/19.
 * 原文出处：androidxx.cn
 */
public class CustomScrollView extends ScrollView{

    private IScrollChangedListener scrollChangedListener;

    public void setScrollChangedListener(IScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }

    public CustomScrollView(Context context) {
        this(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollChangedListener.onScrollChanged(l,t,oldl,oldt);
    }

    interface IScrollChangedListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
