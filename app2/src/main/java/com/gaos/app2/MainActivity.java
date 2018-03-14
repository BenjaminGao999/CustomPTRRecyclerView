package com.gaos.app2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.gaos.app2.CustomViews.MyItemDecoration;
import com.gaos.app2.CustomViews.PullToRefreshRelativeLayout;
import com.gaos.app2.adapters.MyAdapter;
import com.gaos.app2.interfaces.IPullToRefreshManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> strings;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyItemDecoration());
        strings = new ArrayList<>();
        adapter.setData(strings);

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

        getData(20);


    }

    private void getData(int account) {
        for (int i = 0; i < account; i++) {
            strings.add("Item+" + i);
        }
        adapter.notifyDataSetChanged();
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
