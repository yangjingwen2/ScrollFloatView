package com.android.yangjw.mylesson01;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    private ListView mListView;
    private List<String> mDataSource = new ArrayList<>();
    private MyAdatper myAdatper;
    private TextView mInvisibleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
        mInvisibleView = (TextView) findViewById(R.id.invisible_view);
        setData();
        myAdatper = new MyAdatper(this);
        mListView.setAdapter(myAdatper);
        mListView.setOnScrollListener(this);
    }

    private void setData() {
        for (int i = 0; i < 30; i++) {
            mDataSource.add("item" + i);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        Log.d("yangjw","--" + firstVisibleItem);

        if (firstVisibleItem >= 6) {
            if (mInvisibleView.getVisibility() == View.GONE) {
                mInvisibleView.setText("item6");
                mInvisibleView.setVisibility(View.VISIBLE);
            }
        } else {
            mInvisibleView.setVisibility(View.GONE);
        }
    }

    class MyAdatper extends BaseAdapter {

        private Context context;

        public MyAdatper(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mDataSource == null ? 0:mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.itemTxt.setText(mDataSource.get(position));

            return view;
        }

        class ViewHolder {

            public TextView itemTxt;
            public ViewHolder(View view) {
                itemTxt = (TextView) view.findViewById(R.id.item_text);
            }
        }
    }
}
