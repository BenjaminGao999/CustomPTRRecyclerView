package com.adnonstop.combinationptrrv.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnonstop.combinationptrrv.R;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/20 19:01
 * versionCode:　v2.2
 * <p>
 * 实现对 headerView 的定制
 */

public class PullToRefreshLayoutDemo extends PullToRefreshLayout {
    public PullToRefreshLayoutDemo(Context context) {
        this(context, null);
    }

    public PullToRefreshLayoutDemo(Context context, AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public PullToRefreshLayoutDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View addHeaderView() {
        View mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.ptr_header_layout, this, false);
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mHeaderView);
        return mHeaderView;
    }
}
