package com.android.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostAsyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_async);
    }

    public void click(View view) {
        OkHttpClient okHttpClient = new OkHttpClient();
        //Form表单格式的参数传递
        FormBody formBody = new FormBody
                .Builder()
                .add("username","androidxx.cn")//设置参数名称和参数值
                .build();
        Request request = new Request
                .Builder()
                .post(formBody)//Post请求的参数传递
                .url(Config.LOCALHOST_POST_URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //此方法运行在子线程中，不能在此方法中进行UI操作。
                String result = response.body().string();
                Log.d("androixx.cn", result);
                response.body().close();
            }
        });


    }
}
