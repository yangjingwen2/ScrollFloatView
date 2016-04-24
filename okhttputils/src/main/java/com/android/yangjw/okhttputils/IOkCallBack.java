package com.android.yangjw.okhttputils;

import java.io.IOException;

/**
 * Created by yangjw on 2016/4/24.
 * url：androidxx.cn
 * desc：TODO
 */
public interface IOkCallBack<T> {

    /**
     * 获取Jave bean类型，以提供给Gson进行解析，将Json转换成指定的java bean对象
     * @return 指定返回的数据类型
     */
    public Class<T> getClassType();

    /**
     * 成功返回的数据结果
     * @param result
     */
    public void onSuccess(T result);

    /**
     * 请求失败的异常信息
     * @param e
     */
    public void onException(IOException e);
}
