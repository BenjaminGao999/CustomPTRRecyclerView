package com.gaos.app2.CustomViews;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/13 15:12
 * versionCode:　v2.2
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        float px = dp2px(10, parent.getContext());
        outRect.bottom = (int) px;
    }

    private float dp2px(int dp,
                        Context context) {

        float px = context.getResources().getDisplayMetrics().density * dp;
        return px;
    }
}
