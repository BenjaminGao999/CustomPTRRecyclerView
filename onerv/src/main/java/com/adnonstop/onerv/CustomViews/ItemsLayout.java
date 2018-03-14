package com.adnonstop.onerv.CustomViews;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.adnonstop.onerv.R;

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
    private InnerAdapter adapter;
    public static final int HEADER = 0x01;
    public static final int FOOTER = 0x02;
    public static final int ITEM = 0x03;

    public ItemsLayout(Context context) {
        this(context, null);
    }

    public ItemsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ItemsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate = inflate(context, R.layout.items_layout_layout, this);

        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new InnerAdapter();
        recyclerView.setAdapter(adapter);
        strings = new ArrayList<>();
        adapter.setData(strings);
        recyclerView.addItemDecoration(new InnerItemDecoration());

        //联网获取数据的过程
        getData();
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 模拟加载数据延迟
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 30; i++) {
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


    private class InnerAdapter extends RecyclerView.Adapter {
        private ArrayList<String> data;
        private RelativeLayout footerItem;
        private View itemView;
        private RelativeLayout headerItem;
        private LinearLayoutManager layoutManager;
        private float dy;
        private int newState;
        private boolean FLAGS_INIT_HEADER = true;
        private RecyclerView.LayoutParams paramsHeader;

        @Override
        public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
//                    printNewState(newState);

//                    onDraggingLis(newState);

                    InnerAdapter.this.newState = newState;
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
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                    Log.i(TAG, "onScrolled: dx = " + dx + " ; dy = " + dy);

                    if (FLAGS_INIT_HEADER) {
                        hideHeaderItem();
                        FLAGS_INIT_HEADER = false;
                    }
                }


            });


            recyclerView.setOnTouchListener(new OnTouchListener() {

                private float rawY_down;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    printMotionEvent(event);
                    calculationDy(event);

                    return false;
                }

                private void calculationDy(MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dy = 0;
                            rawY_down = event.getRawY();

                            break;
                        case MotionEvent.ACTION_MOVE:
                            float rawY_move = event.getRawY();
                            dy += rawY_move - rawY_down;
                            rawY_down = rawY_move;

                            onDraggingLis(newState);

                            break;
                        case MotionEvent.ACTION_UP:

                            onActionUP();

                            break;
                        default:
                            break;

                    }
                }

                private void onActionUP() {
                    if (paramsHeader != null) {

                        if (paramsHeader.topMargin < (-headerItem.getMeasuredHeight() / 2.0f)) {//不刷新
                            hideHeaderItem();

                        } else {// 刷新
                            paramsHeader.topMargin = 0;
                            headerItem.setLayoutParams(paramsHeader);
                            // 模拟刷新数据
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideHeaderItem();
                                }
                            }, 1000);
                        }
                    }
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

                private void onDraggingLis(int newState) {

                    // 上拉加载更多
                    //1. newState是Dragging
                    //2. 上拉
                    //3. 最后一条完全可见条目在 那个坐标区域。
                    int lCVIPosition = layoutManager.findLastCompletelyVisibleItemPosition();
//                    Log.i(TAG, "onDraggingLis: "+lCVIPosition);

                    if (lCVIPosition == layoutManager.getItemCount() - 1) {
                        if (footerItem != null && footerItem.getMeasuredHeight() == 0) {
                            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                                if ((dy < -dp2px(10))) {
                                    showFooterItem();

                                }
                            }
                        }
                    }

                    //下拉刷新
                    //1. 第1条 条目要完全可见，
                    // 2. 下拉
                    //   3. newState 要处于Dragging状态

                    int fCVIPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (fCVIPosition + 1 == 1) {
                        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            if (dy > dp2px(10)) {
                                recyclerView.setNestedScrollingEnabled(false);

                                paramsHeader = (RecyclerView.LayoutParams) headerItem.getLayoutParams();
                                paramsHeader.topMargin = (int) (dy / 2.0f) - headerItem.getMeasuredHeight();
                                headerItem.setLayoutParams(paramsHeader);

                            }
                        }
                    }

                }
            });
        }

        private void hideHeaderItem() {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) headerItem.getLayoutParams();
            int measuredHeight = headerItem.getMeasuredHeight();
            params.topMargin = -measuredHeight;
            headerItem.setLayoutParams(params);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            int tempId = R.layout.item_items_layout;
            View temp = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_items_layout, parent, false);

            switch (viewType) {
                case HEADER:
//                    tempId = R.layout.item_header_layout;
                    headerItem = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_layout, parent, false);
                    temp = headerItem;
//                    RecyclerView.LayoutParams paramsHeader = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    headerItem.setLayoutParams(paramsHeader);
                    break;
                case ITEM:
//                    tempId = R.layout.item_items_layout;
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_items_layout, parent, false);
                    temp = itemView;
                    break;
                case FOOTER:
//                    tempId = R.layout.item_footer_layout;
                    footerItem = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_layout, parent, false);
                    temp = footerItem;
                    footerItem.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
                    break;
                default:
                    break;
            }

            return new InnerViewHolder(temp);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == ITEM) {
                InnerViewHolder holder1 = (InnerViewHolder) holder;
                holder1.tvItem.setText(data.get(position - 1));
            } else if (getItemViewType(position) == FOOTER) {
//                holder.itemView.setVisibility(GONE);
//                footerItem.setVisibility(GONE);
//                showFooterItem();

            } else if (getItemViewType(position) == HEADER) {
                // 头部
//                Log.i(TAG, "onBindViewHolder: headerItem.getMeasuredHeight()= "+headerItem.getMeasuredHeight());

            }
        }

        private void showFooterItem() {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) footerItem.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            footerItem.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            return (data == null || data.size() == 0) ? 0 : data.size() + 2;
        }

        public void setData(ArrayList<String> data) {
            this.data = data;
        }

        @Override
        public int getItemViewType(int position) {
            int temp = ITEM;

            if (position == 0) {
                temp = HEADER;
            } else if (position == getItemCount() - 1) {
                temp = FOOTER;
            } else {
                temp = ITEM;
            }
//            return super.getItemViewType(position);
            return temp;
        }

        private class InnerViewHolder extends RecyclerView.ViewHolder {

            public TextView tvItem;

            public InnerViewHolder(View itemView) {
                super(itemView);
                tvItem = (TextView) itemView.findViewById(R.id.tv_item);
            }


        }
    }

    private class InnerItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = (int) dp2px(10);
//            Log.i(TAG, "getItemOffsets: " + dp2px(10));
        }
    }

    private float dp2px(int dp) {
        float px = getContext().getResources().getDisplayMetrics().density * dp;
        return px;
    }


}
