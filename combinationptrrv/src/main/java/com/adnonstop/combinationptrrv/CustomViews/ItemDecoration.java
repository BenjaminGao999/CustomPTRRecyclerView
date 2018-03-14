package com.adnonstop.combinationptrrv.CustomViews;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/14 9:46
 * versionCode:　v2.2
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {


    private float dp2px(int dp, Context context) {
        float px = 0;
        if (context != null) {
            px = context.getResources().getDisplayMetrics().density * dp;
        }
        return px;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = (int) dp2px(10, parent.getContext());
//            Log.i(TAG, "getItemOffsets: " + dp2px(10));
    }
}
