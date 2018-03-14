package com.adnonstop.combinationptrrv.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import com.adnonstop.combinationptrrv.R;
import com.adnonstop.combinationptrrv.adapters.MyAdapter;
import com.adnonstop.combinationptrrv.utils.Dp2px;

import java.util.ArrayList;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/13 12:03
 * versionCode:　v2.2
 */

public class ItemsLayout extends FrameLayout {
    private static final String TAG = "ItemsLayout";
    private View inflate;
    private ArrayList<String> strings;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private int newState;
    private LinearLayoutManager layoutManager;
    private RelativeLayout rlHeaderView;
    public float dy;
    /*
    * 作为一个flag, 在最开始走onMeasure时， 把 头布局 隐藏起来的，
    * 在后面显示和隐藏 头布局的时候， 要根据这个flag， 不走 把头布局隐藏起来的代码的。
    * 在onMeasure方法中 隐藏头部局有个好处， 可以获取到头布局的测量高度。
    * */
    private boolean isDraggingShow;
    private LayoutParams rlHeaderViewLayoutParams;
    private int rlHeaderViewMeasuredHeight;
    private LayoutParams recyclerViewLayoutParams;
    private Handler handler = new Handler(Looper.getMainLooper());
    /*在触摸事件当中， action_down, action_move, action_up , 在action_move过程中， 有过在onTouch中完全的夺取了action_move事件，
    * 在action_up发生后， 作为RecyclerView本身的条目滚动， 接收到了Action_down, 然后部分接收到了 Action_move事件， 在Action_up
    * 事件发生后， 就导致了， 在action_down和 action_up 坐标点之间的距离差， 会导致 RecyclerView条目 的滑动 情况，
    * 就会导致 item0 突然的滑动跳跃， 为了避免这种情况， 就在 如果RecyclerView条目本身接收不到 action_move 的所有事件时， 就选择让
    * RecyclerView也接收不到 action_up, 这样就刻意避免了， 手指抬起 item0突然向上 滚动跳跃的情况。
    * */
    private boolean isTriggered;
    /*
    * 在重新定位头布局和RecyclerView的position时， 在执行动画的过程中， 通过阻断 触摸事件，
    * 阻止 RecyclerView的条目 滚动。
    *
    * */
    private boolean keepIntercepted;


    public ItemsLayout(Context context) {
        this(context, null);
    }

    public ItemsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ItemsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate = inflate(context, R.layout.ptr_recycler_view_layout, this);

        recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        strings = new ArrayList<>();
        adapter.setData(strings);
        recyclerView.addItemDecoration(new ItemDecoration());

        //联网获取数据的过程
        getData(3, 1000);

        addListener();

        rlHeaderView = (RelativeLayout) inflate.findViewById(R.id.ptr_header_view);
    }

    private void addListener() {
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                printNewState(newState);
                adapter.onScrollStateChanged(recyclerView, newState);

                ItemsLayout.this.newState = newState;

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.i(TAG, "onScrolled: dx = " + dx + " ; dy = " + dy);
                adapter.onScrolled(recyclerView, dx, dy);
            }

        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            private float rawY_down;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                printMotionEvent(event);
                adapter.onTouch(v, event);
                if (keepIntercepted) {

                    return true;

                } else {

                    return calculationDy(event);
                }
//                return false;
            }

            private boolean calculationDy(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dy = 0;
                        rawY_down = event.getRawY();
                        isTriggered = false;

//                        break;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        float rawY_move = event.getRawY();
                        dy += rawY_move - rawY_down;
                        rawY_down = rawY_move;

                        return onDraggingLis(newState);

//                        break;

                    case MotionEvent.ACTION_UP:
                        onActionUp();
//                        break;
                        if (isTriggered) {
                            return true;
                        }


                    default:
                        break;

                }
                return false;
            }

            private void onActionUp() {
                if (rlHeaderViewLayoutParams.topMargin >= 0) {// 刷新
//                    rlHeaderViewLayoutParams.topMargin = 0;
//                    recyclerViewLayoutParams.topMargin = rlHeaderViewMeasuredHeight;
                    final int dy = rlHeaderViewLayoutParams.topMargin;
                    onActionUp(dy, true);

                    // 加载数据期间， 拦截一切， 触摸事件
//                    keepIntercepted = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

//                            rlHeaderViewLayoutParams.topMargin = -rlHeaderViewMeasuredHeight;
//                            recyclerViewLayoutParams.topMargin = 0;
//                            recyclerView.requestLayout();

                            onActionUp(rlHeaderViewMeasuredHeight, false);
//                            keepIntercepted = false;
                            getData(4, 0);

                        }
                    }, 1000);

                } else { // 不刷新
//                    rlHeaderViewLayoutParams.topMargin = -rlHeaderViewMeasuredHeight;
//                    recyclerViewLayoutParams.topMargin = 0;
//                    recyclerView.requestLayout();
                    int dy = rlHeaderViewLayoutParams.topMargin + rlHeaderViewMeasuredHeight;
                    onActionUp(dy, false);
                }
            }

            /**
             *
             * @param dy
             * @param keepInterceptedAsync 当确定要刷新， 必须要在数据获取到，并把 头布局 隐藏起来， 动画执行结束， 才能取消阻断 RecyclerView的条目滚动
             */
            private void onActionUp(final int dy, final boolean keepInterceptedAsync) {

                if (dy != 0) {
                    final int topMarginHeaderOrigin = rlHeaderViewLayoutParams.topMargin;
                    final int topMarginRecyclerViewOrigin = recyclerViewLayoutParams.topMargin;

                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1).setDuration(200);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float animatedFraction = animation.getAnimatedFraction();
//                            Log.i(TAG, "onAnimationUpdate: "+animatedFraction);

                            rlHeaderViewLayoutParams.topMargin = (int) (topMarginHeaderOrigin - dy * animatedFraction);
                            recyclerViewLayoutParams.topMargin = (int) (topMarginRecyclerViewOrigin - dy * animatedFraction);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.requestLayout();
                                }
                            });

                            // 动画期间 拦截一切 触摸事件
                            if ((animatedFraction < 1.0)) {
                                keepIntercepted = true;
                            } else {
                                if (!keepInterceptedAsync) {
                                    keepIntercepted = false;
                                }
                            }
                        }
                    });
                    valueAnimator.start();
                }
            }


            private boolean onDraggingLis(int newState) {

                // 上拉加载更多
                //1. newState是Dragging
                //2. 上拉
                //3. 最后一条完全可见条目在 那个坐标区域。


                //下拉刷新
                //1. 第1条 条目要完全可见，
                // 2. 下拉
                //   3. newState 要处于Dragging状态, 非必要的条件

                int fCVIPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (fCVIPosition == 0) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    }

                    //只有下拉拖拽到一定阈值， 才能判定为刷新
                    if (dy > Dp2px.dp2px(30, getContext()) && !isTriggered) {
                        isDraggingShow = true;
                        isTriggered = true;
                        dy = 0;
                    }

                    if (dy > Dp2px.dp2px(0, getContext()) && isTriggered) {
//                            Log.i(TAG, "onDraggingLis: 可以下拉了");
                        rlHeaderViewLayoutParams.topMargin = (int) (-rlHeaderViewMeasuredHeight + (dy / 1.5f));
//                            rlHeaderView.setLayoutParams(params);
//                            Log.i(TAG, "onDraggingLis: " + params.topMargin);
//                            rlHeaderView.requestLayout();

                        recyclerViewLayoutParams.topMargin = (int) (dy / 1.5f);
                        recyclerView.requestLayout();

                        return true;

                    } else if (dy <= Dp2px.dp2px(0, getContext()) && isDraggingShow && isTriggered) {
                        rlHeaderViewLayoutParams.topMargin = -rlHeaderViewMeasuredHeight;
                        recyclerViewLayoutParams.topMargin = 0;
                        rlHeaderView.requestLayout();
                    }
                }

                return false;
            }
        });

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

    private void printNewState(int newState) {
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.i(TAG, "onMeasure: rlHeaderView.getMeasuredHeight() = "+rlHeaderView.getMeasuredHeight());
        if (!isDraggingShow) {
            rlHeaderViewLayoutParams = (LayoutParams) rlHeaderView.getLayoutParams();
            rlHeaderViewMeasuredHeight = rlHeaderView.getMeasuredHeight();
            rlHeaderViewLayoutParams.topMargin = -rlHeaderViewMeasuredHeight;
            rlHeaderView.setLayoutParams(rlHeaderViewLayoutParams);

            // 初始RecyclerView LayoutParams实例。
            recyclerViewLayoutParams = (LayoutParams) recyclerView.getLayoutParams();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Log.i(TAG, "onLayout: rlHeaderView.getMeasuredHeight() = "+rlHeaderView.getMeasuredHeight());
//        Log.i(TAG, "onLayout: rlHeaderView.getTop() = "+rlHeaderView.getTop());
//        Log.i(TAG, "onLayout: recyclerView.getTop() = "+recyclerView.getTop());
//        Log.i(TAG, "onLayout: left = "+left+" ;top = "+top+" ;right = "+right+" ;bottom = "+bottom);
    }

    private void getData(final int dataSize, final int sleepTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 模拟加载数据延迟
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < dataSize; i++) {
                    strings.add("item " + i);
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setData(strings);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

}
