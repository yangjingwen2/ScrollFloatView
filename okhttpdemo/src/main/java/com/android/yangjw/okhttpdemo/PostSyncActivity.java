package com.android.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class PostSyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sync);
    }

    public void click(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                formPost();
            }
        }).start();

    }

    public void click2(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonPost();
            }
        }).start();

    }

    private void formPost() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //Form表单格式的参数传递
        FormBody formBody = new FormBody
                .Builder()
                .add("username","androidxx")//设置参数名称和参数值
                .build();
        Request request = new Request
                .Builder()
                .post(formBody)//Post请求的参数传递
                .url(Config.LOCALHOST_POST_URL)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            Log.d("androixx",result);
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jsonPost() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //设置媒体类型。application/json表示传递的是一个json格式的对象
        MediaType mediaType = MediaType.parse("application/json");
        //使用JSONObject封装参数
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username","androidxx");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //创建RequestBody对象，将参数按照指定的MediaType封装
        RequestBody requestBody = RequestBody.create(mediaType,jsonObject.toString());
        Request request = new Request
                .Builder()
                .post(requestBody)//Post请求的参数传递
                .url(Config.LOCALHOST_POST_URL)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            Log.d("androixx", result);

            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
