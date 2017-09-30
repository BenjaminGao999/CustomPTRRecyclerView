package com.gaos.app2;

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
    //    private boolean isloadmore;
    private float rawY;
    private float dy;
    private IPullToRefreshManager.IPullToRefresh mIPullToRefresh;
    private int mHeaderViewMeasuredHeight;
    private int mFooterViewMeasuredHeight;
    private float disY2;
    private float mRecyclerViewCurrentMoveY2;
    private LinearLayoutManager recyclerViewLayoutManager;
    private boolean isFooterViewShow;
    //    private boolean isInit = true;
    private int dyScrolled;


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

        IPullToRefreshManager.setiPullToRefreshResult(iPullToRefreshResult);
    }


    private void initView() {
        if (mRecyclerView != null) {

            onRecyclerViewItemFinished();

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    /**
                     * 只有当 RecyclerView 状态 发生改变才会调用该方法
                     */
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_DRAGGING ");
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_IDLE ");

                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_SETTLING ");
                    }

                    newStateF = newState;

//                    judgeloadmoreEnable(newState);

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    /**
                     *只有当列表 滚动 时，才会调用该方法
                     */

                    Log.e(TAG, "onScrolled: dy = " + dy);

//                    hideFooterView(dy);
                    dyScrolled = dy;
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
//                            Log.e(TAG, "onTouch: recyclerview ACTION_MOVE ");
                            judgeRefreshEnable(event);
//                            judgeloadmoreEnable(newStateF);
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e(TAG, "onTouch: recyclerview ACTION_UP ");
//                            isloadmore = false;
                            isRefresh = false;
                            dy = 0;
                            disY = 0;
                            disY2 = 0;
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

    private void onRecyclerViewItemFinished() {
//        if (recyclerViewLayoutManager == null) {
//            recyclerViewLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//        }
//        boolean firstBoolean = recyclerViewLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
//        boolean lastBoolean = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerViewLayoutManager.getItemCount() - 1;
//        if (firstBoolean && lastBoolean) {
//
//        }

//        /**
//         * 09-30 16:55:42.639 27915-27915/? E/PullToRefreshRelativeLa: onScrolled: lastVisiableItemPosition = 10
//         09-30 16:55:42.639 27915-27915/? E/PullToRefreshRelativeLa: onScrolled: lastCompletelyVisibleItemPosition = 9
//         09-30 16:55:42.639 27915-27915/? E/PullToRefreshRelativeLa: onScrolled: lastView bottom = 1980
//         09-30 16:55:42.639 27915-27915/? E/PullToRefreshRelativeLa: onScrolled: recycler view bottom = 1866
//         09-30 16:55:42.639 27915-27915/? E/PullToRefreshRelativeLa: onScrolled: lastCompleteVisiableView bottom = 1800
//         09-30 16:55:42.639 27915-27915/? E/PullToRefreshRelativeLa: onScrolled: recycler view bottom = 1866
//         */
//        int lastVisibleItemPosition = recyclerViewLayoutManager.findLastVisibleItemPosition();
//        int lastCompletelyVisibleItemPosition = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
//        View lastVisiableView = recyclerViewLayoutManager.findViewByPosition(lastVisibleItemPosition);
//        Log.e(TAG, "onScrolled: lastVisiableItemPosition = " + lastVisibleItemPosition);
//        Log.e(TAG, "onScrolled: lastCompletelyVisibleItemPosition = " + lastCompletelyVisibleItemPosition);
//        View lastCompleteVisiableView = recyclerViewLayoutManager.findViewByPosition(lastCompletelyVisibleItemPosition);
//        if (lastVisiableView != null) {
//            Log.e(TAG, "onScrolled: lastView bottom = " + lastVisiableView.getBottom());
//            Log.e(TAG, "onScrolled: recycler view bottom = " + mRecyclerView.getBottom());
//        }
//        if (lastCompleteVisiableView != null) {
//            Log.e(TAG, "onScrolled: lastCompleteVisiableView bottom = " + lastCompleteVisiableView.getBottom());
//            Log.e(TAG, "onScrolled: recycler view bottom = " + mRecyclerView.getBottom());
//        }

        if (recyclerViewLayoutManager == null) {
            recyclerViewLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        }
        int lastVisibleItemPosition = recyclerViewLayoutManager.findLastVisibleItemPosition();
        int lastCompletelyVisibleItemPosition = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
        boolean lastBoolean = lastVisibleItemPosition == recyclerViewLayoutManager.getItemCount() - 1;
        boolean lastCompleteBoolean = lastCompletelyVisibleItemPosition == recyclerViewLayoutManager.getItemCount() - 1;
        if (lastBoolean) {
            if (lastCompleteBoolean) {//FooterView hide
                if (IPullToRefreshManager.iFooterViewManager != null) {

                    IPullToRefreshManager.iFooterViewManager.showEmpty();
                }

            } else {// FooterView 显示“没有更多数据了”

                if (IPullToRefreshManager.iFooterViewManager != null) {
                    IPullToRefreshManager.iFooterViewManager.showNone();
                }
            }
        } else {//FooterView 加载中...

            if (IPullToRefreshManager.iFooterViewManager != null) {
                IPullToRefreshManager.iFooterViewManager.showloading();
            }
        }

    }

    private void hideFooterView(int dy) {
        if (newStateF == RecyclerView.SCROLL_STATE_DRAGGING) {
            if (isFooterViewShow) {
                if (dy < 0) {
                    isFooterViewShow = false;
                    requestLayout();
                }
            }
        }
    }

    private void judgeloadmoreEnable(int newState) {
        if (dyScrolled > 0) {
            if (recyclerViewLayoutManager == null) {
                recyclerViewLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                boolean lastBoolean = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerViewLayoutManager.getItemCount() - 1;
//                            boolean firstBoolean = recyclerViewLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
                if (lastBoolean && !isFooterViewShow) {
                    int lastCompletelyVisibleItemPosition = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
                    View childAtLast = recyclerViewLayoutManager.findViewByPosition(lastCompletelyVisibleItemPosition);
                    if (childAtLast != null) {
//                                        int itemMeasuredHeight = childAtLast.getMeasuredHeight();
                        int childAtLastBottom = childAtLast.getBottom();
                        int recyclerViewBottom = mRecyclerView.getBottom();

                        if (recyclerViewBottom == childAtLastBottom) {//最后一个Item刚好完全可见
                            isFooterViewShow = true;
                            requestLayout();
                            mRecyclerView.scrollToPosition(recyclerViewLayoutManager.getItemCount() - 1);
                        }
                    }
                }

            }
        }
    }

    private void judgeRefreshEnable(MotionEvent event) {
        /**
         * 刷新 触发条件
         */

        if (recyclerViewLayoutManager == null) {
            recyclerViewLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        }
        if (newStateF == RecyclerView.SCROLL_STATE_DRAGGING) {
//                                boolean lastBoolean = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerViewLayoutManager.getItemCount() - 1;
            boolean firstBoolean = recyclerViewLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
//                                if (firstBoolean && !lastBoolean) {
//                                    isRefresh = true;
//
//                                } else if (firstBoolean && lastBoolean) {
//
//                                    mRecyclerViewCurrentMoveY = event.getRawY();
//                                    disY += (mRecyclerViewCurrentMoveY - mRawDownY) / 3.0f;//10倍的阻尼系数
//                                    mRawDownY = mRecyclerViewCurrentMoveY;
//
//                                    if (disY > 30) {
//
//                                        isRefresh = true;
//                                    }
//
//                                    Log.e(TAG, "onTouch: disY = " + disY);
//                                }

            if (firstBoolean) {

                mRecyclerViewCurrentMoveY = event.getRawY();
                disY += (mRecyclerViewCurrentMoveY - mRawDownY) / 3.0f;//10倍的阻尼系数
                mRawDownY = mRecyclerViewCurrentMoveY;

                if (disY > 30) {

                    isRefresh = true;
                }

                Log.e(TAG, "onTouch:isRefresh  disY = " + disY);
            }
        }

//                            /**
//                             *加载更多 触发条件
//                             */
//                            if (newStateF == RecyclerView.SCROLL_STATE_DRAGGING) {
//                                boolean lastBoolean = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerViewLayoutManager.getItemCount() - 1;
//                                if (lastBoolean) {
//                                    int lastCompletelyVisibleItemPosition = recyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
//                                    View childAtLast = recyclerViewLayoutManager.findViewByPosition(lastCompletelyVisibleItemPosition);
//
//                                    if (childAtLast != null) {
////                                        int itemMeasuredHeight = childAtLast.getMeasuredHeight();
//                                        int childAtLastBottom = childAtLast.getBottom();
//                                        int recyclerViewBottom = mRecyclerView.getBottom();
//
//                                        if (recyclerViewBottom == childAtLastBottom) {
//
//                                            mRecyclerViewCurrentMoveY2 = event.getRawY();
//                                            disY2 += (mRecyclerViewCurrentMoveY2 - mRawDownY) / 3.0f;//10倍的阻尼系数
//                                            mRawDownY = mRecyclerViewCurrentMoveY2;
//
//                                            if (disY2 < -30) {
//
//                                                isloadmore = true;
//                                            }
//
//                                            Log.e(TAG, "onTouch: isloadmore disY = " + disY2);
//
//                                        }
//                                    }
//                                }
//
//                            }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);

        mRecyclerView = (RecyclerView) getChildAt(0);
        mHeaderView = getChildAt(1);
        mFooterView = getChildAt(2);

        mHeaderViewMeasuredHeight = mHeaderView.getMeasuredHeight();
        mFooterViewMeasuredHeight = mFooterView.getMeasuredHeight();

//        mRecyclerView.layout(l, (int) (t + dy), r, (int) (b + dy));
        mHeaderView.layout(l, (int) (-mHeaderViewMeasuredHeight + dy), r, (int) dy);
//        mFooterView.layout(l, (int) (b + dy), r, (int) (b + mFooterViewMeasuredHeight + dy));
        Log.e(TAG, "onLayout: l = " + l + " ; t = " + t + " ; r = " + r + " ;  b = " + b);

        if (isFooterViewShow) {
            mRecyclerView.layout(l, t, r, b - mFooterViewMeasuredHeight);
            mFooterView.layout(l, b - mFooterViewMeasuredHeight, r, b);
//            mRecyclerView.scrollToPosition(recyclerViewLayoutManager.getItemCount() - 1);
            Log.e(TAG, "onLayout: mRecyclerView Bottom = " + mRecyclerView.getBottom());
        } else {
            mRecyclerView.layout(l, (int) (t + dy), r, (int) (b + dy));
            mFooterView.layout(l, (int) (b + dy), r, (int) (b + mFooterViewMeasuredHeight + dy));
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRawDownY = ev.getRawY();
//                Log.e(TAG, "onInterceptTouchEvent: ACTION_DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "onInterceptTouchEvent: ACTION_MOVE ");
                break;
            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "onInterceptTouchEvent: ACTION_UP ");
                break;
            default:
                break;
        }

//        if (isRefresh || isloadmore) {
//            return true;
//        }
        if (isRefresh) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.e(TAG, "onTouchEvent: ACTION_DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "onTouchEvent: ACTION_MOVE");
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
//                Log.e(TAG, "onTouchEvent: ACTION_UP");
//                isloadmore = false;
//                isRefresh = false;
//                dy = 0;
                disY = 0;
//                requestLayout();
                disY2 = 0;

                onRefreshUp();

                break;
            default:
                break;
        }

        return true;
    }


    private void onRefreshUp() {
        if (dy < mHeaderViewMeasuredHeight) {
            dy = 0;
            isRefresh = false;
        } else {
            dy = mHeaderViewMeasuredHeight;
            if (mIPullToRefresh != null) {
                mIPullToRefresh.onRefresh();
            } else {
                isRefresh = false;
            }
        }

        requestLayout();
    }

    public void setIPullToRefresh(IPullToRefreshManager.IPullToRefresh iPullToRefresh) {
        mIPullToRefresh = iPullToRefresh;
    }

    private IPullToRefreshManager.IPullToRefreshResult iPullToRefreshResult = new IPullToRefreshManager.IPullToRefreshResult() {
        @Override
        public void onRefreshResult(IPullToRefreshManager.EPullToRefreshResult ePullToRefreshResult) {
            dy = 0;
            requestLayout();
            isRefresh = false;
        }

        @Override
        public void onloadmoreResult(IPullToRefreshManager.EPullToRefreshResult ePullToRefreshResult) {

        }
    };
}
