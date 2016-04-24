package com.android.yangjw.okhttputils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by yangjw on 2016/4/24.
 * url：androidxx.cn
 * desc：TODO
 */
public class GsonUtil {

    public static Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json,clazz);
    }
}
