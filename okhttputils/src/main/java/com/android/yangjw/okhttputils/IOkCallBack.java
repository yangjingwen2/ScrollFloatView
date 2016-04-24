package com.android.yangjw.okhttputils;

import java.io.IOException;

/**
 * Created by yangjw on 2016/4/24.
 * url：androidxx.cn
 * desc：TODO
 */
public interface IOkCallBack {

    public void onSuccess(String result);
    public void onException(IOException e);
}
