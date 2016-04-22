package com.android.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetAsyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_async);
    }

    public void click(View view) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Config.URL).build();
        //enqueue就是将此次的call请求加入异步请求队列，会开启新的线程执行，并将执行的结果通过Callback接口回调的形式返回。
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的回调方法
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功的回调方法
                String result = response.body().string();
                Log.d("yangjw",result);
                //关闭body
                response.body().close();
            }
        });
    }
}
