package com.adnonstop.combinationptrrv.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/20 15:53
 * versionCode:　v2.2
 * <p>
 * RecyclerView.onScrollStateChangedListener
 * scroll state log util
 */

public class RecyclerViewScrollStateLogUtil {

    public static void printNewState(int newState, String TAG) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                Log.i(TAG, "printNewState: RecyclerView.SCROLL_STATE_DRAGGING:");

                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                Log.i(TAG, "printNewState: RecyclerView.SCROLL_STATE_IDLE:");

                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                Log.i(TAG, "printNewState: RecyclerView.SCROLL_STATE_SETTLING:");

                break;
        }
    }

}
