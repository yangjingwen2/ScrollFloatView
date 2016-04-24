package com.android.yangjw.okhttputils;

import android.os.Handler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangjw on 2016/4/24.
 * url：androidxx.cn
 * desc：TODO
 */
public class OkHttpUtil {

    //保证OkHttpClient是唯一的
    private static OkHttpClient okHttpClient;

    static Handler mHandler = new Handler();

    static {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
    }

    /**
     * Get请求
     * @param url
     * @param callback 回调函数
     */
    public static void httpGet(String url, final IOkCallBack callback) {

        if (callback == null) throw new NullPointerException("callback is null");
        if (callback.getClassType() == null) throw new NullPointerException("getClassType() cannot return null");
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback(callback));
    }

    /**
     * Post请求
     * @param url
     * @param params 参数
     * @param callback 回调函数
     */
    public static void httpPost(String url,Map<String,String> params,final IOkCallBack callback) {
        if (callback == null) throw new NullPointerException("callback is null");
        if (callback.getClassType() == null) throw new NullPointerException("getClassType() cannot return null");
        if (params == null) throw new NullPointerException("params is null");

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for(String key:keySet) {
            String value = params.get(key);
            formBodyBuilder.add(key,value);
        }
        FormBody formBody = formBodyBuilder.build();

        final Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback(callback));
    }

    private static void post(final IOkCallBack callback, final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(object);
            }
        });
    }

    private static Callback callback(final IOkCallBack callback) {
        Callback back = new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onException(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();
                if (callback.getClassType() == String.class) {
                    post(callback,result);
                } else {
                    Object o = GsonUtil.fromJson(result, callback.getClassType());
                    post(callback,o);
                }

                response.body().close();
            }
        };

        return back;
    }
}
