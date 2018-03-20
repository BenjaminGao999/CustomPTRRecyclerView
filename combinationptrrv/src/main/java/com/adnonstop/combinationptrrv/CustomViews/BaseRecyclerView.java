package com.adnonstop.combinationptrrv.CustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewOnDispatchTouchEvent;
import com.adnonstop.combinationptrrv.utils.Dp2px;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/19 18:30
 * versionCode:　v2.2
 * <p>
 * 实现 dispatchTouchEvent 事件传递给父布局
 */

public class BaseRecyclerView extends RecyclerView implements View.OnTouchListener {
    private static final String TAG = "MyRecyclerView";
    private float rawY_down;
    private float dy;
    private IRecyclerViewOnDispatchTouchEvent iRecyclerViewOnDispatchTouchEvent;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            rawY_down = ev.getRawY();
//            dy = 0;
//        }

        if (iRecyclerViewOnDispatchTouchEvent != null) {

            iRecyclerViewOnDispatchTouchEvent.dispatchTouchEvent(ev);
        }

        return super.dispatchTouchEvent(ev);
    }

    private void printMotionEvent(MotionEvent event) {

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

    private boolean calculationDy(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false;

            case MotionEvent.ACTION_MOVE:
                float rawY_move = event.getRawY();
                dy += rawY_move - rawY_down;
                rawY_down = rawY_move;

                Log.i(TAG, "calculationDy: dy = " + dy);
                if (((dy <= Dp2px.dp2px(-30, getContext())))) {
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                break;
            default:
                break;

        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        return calculationDy(event);
        return false;
    }

    public void setIRecyclerViewOnDispatchTouchEvent(IRecyclerViewOnDispatchTouchEvent iRecyclerViewOnDispatchTouchEvent) {
        this.iRecyclerViewOnDispatchTouchEvent = iRecyclerViewOnDispatchTouchEvent;
    }
}
