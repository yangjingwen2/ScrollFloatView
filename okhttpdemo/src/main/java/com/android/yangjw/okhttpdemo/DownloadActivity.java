package com.android.yangjw.okhttpdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件下载
 * androidxx.cn
 * created by yangjw at 2016.4.12
 */
public class DownloadActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    //准备下载
    public static final int BEGIN = 0;
    //正在下载
    public static final int DOWNLOADING = 1;
    //结束下载
    public static final int END = 2;
    //下载的进度
    private static int progress;
    //是否停止下载
    private boolean cancel ;

    OkHttpClient okHttpClient = new OkHttpClient();
    MyHandler mHandler = new MyHandler(this);
    private ImageView mShowImage;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mProgressBar = (ProgressBar) findViewById(R.id.down_progress_bar);
        mShowImage = (ImageView) findViewById(R.id.down_image);
    }

    public void click2(View view) {
        cancel = true;
    }

    public void click(View view) {

        cancel = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //实例化Builder对象
                Request.Builder builder = new Request.Builder();
                //设置Url
                builder.url(Config.IMAGE_URL);
                //获取已经下载的大小
                int size = baos.size();
                //size表示已经下载的大小。如果不为0，则进行断点续传。
                if (size > 0) {
                    //设置断点续传的开始位置，格式bytes=123456-
                    builder.header("Range", "bytes=" + size + "-");
                    //设置ProgressBar的当前进度从停止位置开始
                    progress = size;
                }
                //创建Request对象
                Request request = builder.build();
                try {
                    //执行下载请求，并获得Response对象
                    Response response = okHttpClient.newCall(request).execute();
                    //请求成功
                    if (response.isSuccessful()) {
                        //从Response对象中获取输入流对象
                        InputStream inputStream = response.body().byteStream();
                        //size==0表示第一次下载，非断点续传
                        if (size == 0) {
                            //获取文件的大小
                            int contentLength = (int) response.body().contentLength();
                            //将文件总大小通过Handler传递到UI线程，设置ProgressBar的总进度值
                            mHandler.obtainMessage(BEGIN,contentLength,0).sendToTarget();
                        }

                        int len = 0;
                        byte[] buffer = new byte[1024];
                        //循环读取文件流，开始下载
                        while((len = inputStream.read(buffer)) != -1) {
                            if (cancel) {
                                //如果点击了停止按钮，cancel为true。则结束循环
                                break;
                            }
                            //将流写入缓存
                            baos.write(buffer,0,len);
                            baos.flush();
                            //发送下载进度
                            mHandler.obtainMessage(DOWNLOADING,len,0).sendToTarget();
                        }
                        //下载完成，结束请求，关闭body
                        response.body().close();

                        //将字节转成Bitmap对象
                        byte[] bytes = baos.toByteArray();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        //下载完成通知更新试图
                        mHandler.obtainMessage(END,bitmap).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class MyHandler extends Handler {
        private WeakReference<DownloadActivity> activityWeakReference;

        public MyHandler(DownloadActivity activity) {
            this.activityWeakReference = new WeakReference<DownloadActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BEGIN:
                    activityWeakReference.get().mProgressBar.setMax(msg.arg1);
                    break;
                case DOWNLOADING:
                    progress += msg.arg1;
                    activityWeakReference.get().mProgressBar.setProgress(progress);
                    break;
                case END:
                    progress = 0;
                    activityWeakReference.get().mShowImage.setImageBitmap((Bitmap)msg.obj);
                    break;
            }
        }
    }

}
