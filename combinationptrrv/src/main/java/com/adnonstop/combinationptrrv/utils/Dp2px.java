package com.adnonstop.combinationptrrv.utils;

import android.content.Context;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/14 9:52
 * versionCode:　v2.2
 */

public class Dp2px {

    public static float dp2px(int dp, Context context) {
        float px = 0;
        if (context != null) {
            px = context.getResources().getDisplayMetrics().density * dp;
        }
        return px;
    }
}
