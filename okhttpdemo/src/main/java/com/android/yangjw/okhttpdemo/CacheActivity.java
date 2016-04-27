package com.android.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp缓存的使用
 * http://androidxx.cn/forum.php?mod=viewthread&tid=17&extra=page%3D1
 */
public class CacheActivity extends AppCompatActivity {

    OkHttpClient okHttpClient;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        //获得缓存目录
        File cacheFile = getCacheDir();
        okHttpClient = new OkHttpClient
                .Builder()
                .cache(new Cache(cacheFile,4*1024*1024)) //设置缓存目录和缓存大小
                .build();
    }

    public void click(View view) {

        final Request.Builder builder = new Request.Builder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = builder.url("https://publicobject.com/helloworld.txt").build();
                try {
                    Response response1 = okHttpClient.newCall(request).execute();
                    String result1 = null;
                    System.out.println("-code--" + response1.code());
                    if (response1.code() != 504) {
                        result1 = response1.body().string();
                        System.out.println("-result1--" + (result1==null));
                    } else {
                        System.out.println("-androidxx--failure" );
                    }
////                    String result1 = response1.body().string();
////                    System.out.println("-1--" + result1);
                    System.out.println("-1--" + response1.cacheResponse());
                    System.out.println("-1--" + response1.networkResponse());
                    response1.body().close();
                    Request request2 = builder.url("https://publicobject.com/helloworld.txt").cacheControl(CacheControl.FORCE_CACHE).build();

//                    Response response = okHttpClient.cache().get(request);
                    Response response2 = okHttpClient.newCall(request2).execute();
//                    String result2 = response2.body().string();
//                    System.out.println("-2--" + (result2==null));
                    System.out.println("-2--" + response2.cacheResponse());
                    System.out.println("-2--" + response2.networkResponse());
                    response2.body().close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }).start();


    }
}
