package com.adnonstop.combinationptrrv.utils;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/20 15:50
 * versionCode:　v2.2
 * <p>
 * 打印 motionEvent
 */

public class MotionEventLogUtil {

    public static void printMotionEvent(MotionEvent event, String TAG) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "printMotionEvent: MotionEvent.ACTION_DOWN:");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "printMotionEvent: MotionEvent.ACTION_MOVE:");

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "printMotionEvent: MotionEvent.ACTION_UP: ");

                break;
            default:
                break;
        }
    }
}
