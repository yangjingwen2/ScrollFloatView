package com.android.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InterceptorActivity extends AppCompatActivity {

    public static final String TAG = "androidxx";
    OkHttpClient okHttpClient;

    /**
     * 应用拦截器
     */
    Interceptor appInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl url = request.url();
            String s = url.url().toString();
            //---------请求之前-----
            Log.d(TAG,"app interceptor:begin");
            Response  response = null;
            //如果Url中没有包含androidxx关键字，则修改请求链接为http://www.androidxx.cn
            if (s.contains("androidxx")) {
                request = request.newBuilder().url("http://www.androidxx.cn").build();
            }
            response = chain.proceed(request);
            Log.d(TAG,"app interceptor:end");
            //---------请求之后------------
            return response;
        }
    };

    /**
     * 网络拦截器
     */
    Interceptor networkInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //---------请求之前-----
            Log.d(TAG,"network interceptor:begin");
            Response  response = chain.proceed(request);
            Log.d(TAG,"network interceptor:end");
            return response;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interceptor);
        okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(appInterceptor)//Application拦截器
                .addNetworkInterceptor(networkInterceptor)//Network拦截器
                .build();
    }

    public void click(View view) {
        Request request = new Request.Builder().url("http://www.androidxx.cn").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"--" + response.networkResponse());
            }
        });
    }
}
