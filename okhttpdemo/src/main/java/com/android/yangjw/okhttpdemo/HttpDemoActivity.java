package com.android.yangjw.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.yangjw.okhttputils.IOkCallBack;
import com.android.yangjw.okhttputils.OkHttpUtil;

import java.io.IOException;

/**
 * 封装OkHttp请求
 * OkHttp的异步请求方式
 * androidxx.cn。
 */
public class HttpDemoActivity extends AppCompatActivity {

    private TextView mShowText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_demo);
        mShowText = (TextView) findViewById(R.id.http_show_txt);
    }

    public void click(View view) {

        OkHttpUtil.httpGet(Config.URL, new IOkCallBack<ProduectInfo>() {

            @Override
            public Class<ProduectInfo> getClassType() {
                return ProduectInfo.class;
            }

            @Override
            public void onSuccess(ProduectInfo result) {
                mShowText.setText(result.getData().getProducts().get(0).getName());
            }

            @Override
            public void onException(IOException e) {

            }
        });

    }
}
