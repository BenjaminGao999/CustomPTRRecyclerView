package com.gaos.pulltorefreshrecyclerviewdemo;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter();

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            arrayList.add("Item+" + i);
        }

        myRecyclerViewAdapter.setData(arrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(myRecyclerViewAdapter);

        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        PullToRefreshRelativeLayout refreshRelativeLayout = (PullToRefreshRelativeLayout) findViewById(R.id.activity_main);
        refreshRelativeLayout.setIPullToRefresh(new IPullToRefreshManager.IPullToRefresh() {
            @Override
            public void onRefresh() {

                doRefresh();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void doRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                IPullToRefreshManager.iPullToRefreshResult.onRefreshResult(IPullToRefreshManager.EPullToRefreshResult.SUCCESSED);
            }
        }, 1000);
    }
}
