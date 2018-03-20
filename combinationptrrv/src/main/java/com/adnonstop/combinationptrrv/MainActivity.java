package com.adnonstop.combinationptrrv;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adnonstop.combinationptrrv.CustomViews.PullToRefreshLayout;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewLoadMoreData;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewRefreshData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mFLContainer;
    private PullToRefreshLayout pullToRefreshLayout;
    private Handler handler;
    private ArrayList<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mFLContainer = new FrameLayout(this);
        mFLContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(mFLContainer);
        handler = new Handler(Looper.getMainLooper());
        strings = new ArrayList<>();

        addPullToRefreshLayout(mFLContainer);
    }

    private void addPullToRefreshLayout(FrameLayout mFLContainer) {
        if (pullToRefreshLayout == null) {
            pullToRefreshLayout = new PullToRefreshLayout(this);
        }
        pullToRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFLContainer.removeAllViews();
        mFLContainer.addView(pullToRefreshLayout);

        pullToRefreshLayout.setIRecyclerViewLoadMoreData(new IRecyclerViewLoadMoreData() {
            @Override
            public void loadMoreData() {

                getData(5);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this, "加载更多成功", Toast.LENGTH_SHORT).show();

                        pullToRefreshLayout.onLoadMoreDataResult(strings);
                    }
                }, 200);
            }
        });

        pullToRefreshLayout.setIRecyclerViewRefreshData(new IRecyclerViewRefreshData() {
            @Override
            public void refreshData() {
                getData(5);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();

                        pullToRefreshLayout.onDataRefreshed(strings);
                    }
                }, 200);
            }
        });

        // 初始化数据
        getData(6);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                pullToRefreshLayout.initData(strings);
            }
        }, 200);
    }


    private void getData(int dataSize) {

        for (int i = 0; i < dataSize; i++) {
            strings.add("item " + i);
        }
    }


}
