package com.android.yangjw.okhttpdemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 文件上传
 * androidxx.cn
 * created by yangjw at 2016.4.12
 */
public class UploadActivity extends AppCompatActivity {

    public static final int GET_PIC = 1;
    private ImageView mShowImage;
    private Uri uri;
    OkHttpClient okHttpClient = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mShowImage = (ImageView) findViewById(R.id.upload_show_image);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.upload_choose_file:
                choosePic();//选择图片
                break;
            case R.id.upload_start:
                upload();//上传
                break;
        }
    }

    /**
     * 上传
     */
    private void upload() {
        if (uri == null) {
            Toast.makeText(UploadActivity.this, "请先选择文件", Toast.LENGTH_SHORT).show();
            return;
        }
        //设置文件的媒体类型，image/*表示匹配所有的图片文件
        MediaType mediaType = MediaType.parse("image/*");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //文件上传，此处是关键，设置媒体类型为multipart/form-data，表示多种格式的表单数据上传
        builder.setType(MultipartBody.FORM);
        //添加上传的参数username=androidxx
        builder.addFormDataPart("username","androidxx");
        //添加上传的文件。文件是从相册读取的文件流。
        try {
            //获得需要上传的文件流
            InputStream inputStream = getContentResolver().openInputStream(uri);
            int len = 0;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            inputStream.close();
            /*
             * 添加文件到Builder中。如果要实现多文件同时上传，可以添加多个addFormDataPart。
             * 注意：
             * 参数一：上传的文件的标示，同username。也就是可以在服务器端通过upload找到对应的文件流
             * 参数二：文件的名称。上传到服务器之后以此名称命名文件
             * 参数三：需要上传的文件。包含在RequestBody中
             * RequestBody.create方法有多个重载的方法，可以选择不同的数据源。此处选择的是字节形式(baos.toByteArray())的数据眼。
             */
            builder.addFormDataPart("upload", "test.jpg", RequestBody.create(mediaType, baos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建MultipartBody对象，MultipartBody是RequestBody的子类，用于文件上传。
        MultipartBody multipartBody = builder.build();
        Request request = new Request.Builder()
                .url("http://192.168.3.4:8080/WebServer/upload.do")
                .post(multipartBody)
                .build();
        //开始上传。采用Post异步请求的方式
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androidxx.cn","--" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //接受到成功的返回结果
                if (response.isSuccessful()) {
                    Log.d("androidxx.cn","-上传成功-");
                } else {
                    Log.d("androidxx.cn","-失败--" + response.body().string());
                }

            }
        });
    }

    /**
     * 打开相册，选择文件后返回
     */
    private void choosePic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GET_PIC);

    }

    /**
     * 接收选择的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        //获得图片的URI
        uri = data.getData();
        //通过ContentResolver获得图片对象
        ContentResolver contentResolver = getContentResolver();
        InputStream inputStream = null;
        try {
            inputStream = contentResolver.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //将流转换成图片，显示到ImageView中
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        mShowImage.setImageBitmap(bitmap);
    }
}
