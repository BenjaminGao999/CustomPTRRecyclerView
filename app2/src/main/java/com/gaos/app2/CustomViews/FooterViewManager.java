package com.gaos.app2.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.gaos.app2.R;

/**
 * Author:　Created by benjamin
 * DATE :  2017/9/30 17:29
 * versionCode:　v2.2
 */

public class FooterViewManager extends FrameLayout {
    public FooterViewManager(Context context) {
        this(context, null);
    }

    public FooterViewManager(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FooterViewManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.footerview_empty, this);
        inflate(context, R.layout.footerview_loading, this);
        inflate(context, R.layout.footerview_none, this);

        showloading();
    }


    public void showEmpty() {

        for (int i = 0; i < getChildCount(); i++) {
            if (i == 0) {
                getChildAt(0).setVisibility(VISIBLE);
            } else {
                getChildAt(i).setVisibility(GONE);
            }
        }
    }

    public void showNone() {

        for (int i = 0; i < getChildCount(); i++) {
            if (i == 2) {
                getChildAt(2).setVisibility(VISIBLE);
            } else {
                getChildAt(i).setVisibility(GONE);
            }
        }
    }

    public void showloading() {
        for (int i = 0; i < getChildCount(); i++) {
            if (i == 1) {
                getChildAt(1).setVisibility(VISIBLE);
            } else {
                getChildAt(i).setVisibility(GONE);
            }
        }
    }
}
