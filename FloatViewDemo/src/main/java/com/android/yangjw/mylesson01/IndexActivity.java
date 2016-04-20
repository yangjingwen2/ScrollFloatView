package com.android.yangjw.mylesson01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class IndexActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        mListView = (ListView) findViewById(R.id.index_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(IndexActivity.this,ListViewActivity.class);
                        break;
                    case 1:
                        intent.setClass(IndexActivity.this,ScrollActivity.class);
                        break;
                }

                startActivity(intent);
            }
        });
    }
}
