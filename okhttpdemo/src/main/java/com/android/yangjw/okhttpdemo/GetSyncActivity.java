package com.android.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp的get同步请求
 */
public class GetSyncActivity extends AppCompatActivity {


    private TextView mShowText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_sync);

        mShowText = (TextView) findViewById(R.id.show_result_txt);
    }

    public void click(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化，在一个APP中建议只需要一个OkHttpClient实例，类似Applcation
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder() //利用建造者模式创建Request对象
                        .url(Config.URL) //设置请求的URL
                        .build(); //生成Request对象

                Response response = null;
                try {
                    //将请求添加到请求队列等待执行，并返回执行后的Response对象
                    response = client.newCall(request).execute();
                    //获取Http Status Code.其中200表示成功
                    if (response.code() == 200) {
                        //这里需要注意，response.body().string()是获取返回的结果，
                        // 此句话只能调用一次，再次调用获得不到结果。
                        //所以先将结果使用result变量接收
                        String result = response.body().string();
                        Log.d("yangjw",result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        }).start();


    }
}
