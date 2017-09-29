package com.gaos.pulltorefreshrecyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;


/**
 * Author:　Created by benjamin
 * DATE :  2017/9/28 20:35
 * versionCode:　v2.2
 */

public class PullToRefreshRelativeLayout extends RelativeLayout {
    private static final String TAG = "PullToRefreshRelativeLa";
    private float mRawDownY;
    private RecyclerView mRecyclerView;
    private View mHeaderView;
    private View mFooterView;
    private int newStateF;
    private boolean isRefresh;
    private float mRecyclerViewCurrentMoveY;
    private float disY;
    private boolean isloadmore;
    private float rawY;
    private float dy;

    public PullToRefreshRelativeLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PullToRefreshRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                initView();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }


    private void initView() {
        if (mRecyclerView != null) {

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    /**
                     * 只有当 RecyclerView 状态 发生改变才会调用该方法
                     */
//                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_DRAGGING ");
//                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_IDLE ");
//
//                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_SETTLING ");
//                    }

                    newStateF = newState;

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    /**
                     *只有当列表 滚动 时，才会调用该方法
                     */

//                    Log.e(TAG, "onScrolled: dy = " + dy);
                }
            });

            mRecyclerView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.e(TAG, "onTouch: recyclerview ACTION_DOWN  ");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.e(TAG, "onTouch: recyclerview ACTION_MOVE ");

                            /**
                             * 刷新 触发条件
                             */
                            LinearLayoutManager recyclerViewLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                            if (newStateF == RecyclerView.SCROLL_STATE_DRAGGING) {
                                boolean lastBoolean = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerViewLayoutManager.getItemCount() - 1;
                                boolean firstBoolean = recyclerViewLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
                                if (firstBoolean && !lastBoolean) {
                                    isRefresh = true;

                                } else if (firstBoolean && lastBoolean) {

                                    mRecyclerViewCurrentMoveY = event.getRawY();
                                    disY += (mRecyclerViewCurrentMoveY - mRawDownY) / 3.0f;//10倍的阻尼系数
                                    mRawDownY = mRecyclerViewCurrentMoveY;

                                    if (disY > 30) {

                                        isRefresh = true;
                                    }

                                    Log.e(TAG, "onTouch: disY = " + disY);
                                }
                            }

                            /**
                             *加载更多 触发条件
                             */
                            if (newStateF == RecyclerView.SCROLL_STATE_DRAGGING) {
                                boolean lastBoolean = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerViewLayoutManager.getItemCount() - 1;
                                if (lastBoolean) {
                                    int lastCompletelyVisibleItemPosition = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
                                    View childAtLast = recyclerViewLayoutManager.findViewByPosition(lastCompletelyVisibleItemPosition);

                                    if (childAtLast != null) {
//                                        int itemMeasuredHeight = childAtLast.getMeasuredHeight();
                                        int childAtLastBottom = childAtLast.getBottom();
                                        int recyclerViewBottom = mRecyclerView.getBottom();

                                        if (recyclerViewBottom == childAtLastBottom) {
                                            isloadmore = true;
                                        }
                                    }
                                }

                            }

                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e(TAG, "onTouch: recyclerview ACTION_UP ");
                            isloadmore = false;
                            isRefresh = false;
                            dy = 0;
                            disY = 0;
                            break;
                        case MotionEvent.ACTION_CANCEL:

                            Log.e(TAG, "onTouch: recyclerview ACTION_CANCEL ");
                            break;
                        default:
                            break;
                    }

                    return false;
                }
            });

        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);

        mRecyclerView = (RecyclerView) getChildAt(0);
        mHeaderView = getChildAt(1);
        mFooterView = getChildAt(2);

        int mHeaderViewMeasuredHeight = mHeaderView.getMeasuredHeight();
        int mFooterViewMeasuredHeight = mFooterView.getMeasuredHeight();


        mRecyclerView.layout(l, (int) (t + dy), r, (int) (b + dy));
        mHeaderView.layout(l, (int) (-mHeaderViewMeasuredHeight + dy), r, (int) dy);
        mFooterView.layout(l, (int) (b + dy), r, (int) (b + mFooterViewMeasuredHeight + dy));

        Log.e(TAG, "onLayout: l = " + l + " ; t = " + t + " ; r = " + r + " ;  b = " + b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRawDownY = ev.getRawY();
                Log.e(TAG, "onInterceptTouchEvent: ACTION_DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onInterceptTouchEvent: ACTION_MOVE ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onInterceptTouchEvent: ACTION_UP ");
                break;
            default:
                break;
        }

        if (isRefresh || isloadmore) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: ACTION_DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: ACTION_MOVE");
                rawY = event.getRawY();
                dy += (rawY - mRawDownY) / 3.0f;
                mRawDownY = rawY;
                if (isRefresh) {
                    if (dy < 0) {
                        dy = 0;
                    }
                }
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent: ACTION_UP");
                isloadmore = false;
                isRefresh = false;
                dy = 0;
                disY = 0;
                requestLayout();
                break;
            default:
                break;
        }

        return true;
    }

}
