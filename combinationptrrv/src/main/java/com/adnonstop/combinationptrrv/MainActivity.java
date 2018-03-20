package com.adnonstop.combinationptrrv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.adnonstop.combinationptrrv.CustomViews.PullToRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mFLContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mFLContainer = new FrameLayout(this);
        mFLContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(mFLContainer);


        addPullToRefreshLayout(mFLContainer);
    }

    private void addPullToRefreshLayout(FrameLayout mFLContainer) {
        PullToRefreshLayout pullToRefreshLayout = new PullToRefreshLayout(this);
        pullToRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFLContainer.addView(pullToRefreshLayout);
    }
}
