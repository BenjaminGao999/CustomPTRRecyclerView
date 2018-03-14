package com.adnonstop.combinationptrrv.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adnonstop.combinationptrrv.R;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewOnTouchListener;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewScrollListener;
import com.adnonstop.combinationptrrv.utils.Dp2px;

import java.util.ArrayList;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/14 9:50
 * versionCode:　v2.2
 */
public class MyAdapter extends RecyclerView.Adapter implements IRecyclerViewScrollListener, IRecyclerViewOnTouchListener {
    private static final String TAG = "MyAdapter";
    private ArrayList<String> data;
    private RelativeLayout footerItem;
    private View itemView;
    private LinearLayoutManager layoutManager;
    private float dy;
    private int newState;

    //    public static final int HEADER = 0x01;
    public static final int FOOTER = 0x02;
    public static final int ITEM = 0x03;
    private RecyclerView recyclerView;
    private float rawY_down;
    /*
    * 总数据量占满了全屏？ true: 占满了， false：未占满。
    * */
    private boolean isFullOccupied = true;
    /*
    * 记录上一次的数据量大小， 上一次和这一次的数据量比较， 如果这一次的数据量大小和上一次大小不一样，
    * 就做一次是否永久隐藏 FooterView的判断。
    * */
    private int previousDataSize;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View temp = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_items_layout, parent, false);

        switch (viewType) {
            case ITEM:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_items_layout, parent, false);
                temp = itemView;
                break;
            case FOOTER:
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM) {
            final InnerViewHolder holder1 = (InnerViewHolder) holder;
            holder1.tvItem.setText(data.get(position));

            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(holder1.itemView.getContext(), ""+data.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (getItemViewType(position) == FOOTER) {

        }

    }

    /**
     * 直接这样写， 会有跳动, 因为要把上面的条目顶上去的
     */
    private void showFooterItem() {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) footerItem.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        footerItem.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0) ? 0 : data.size() + 1;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        int temp;
        if (position == getItemCount() - 1) {
            temp = FOOTER;
        } else {
            temp = ITEM;
        }
        return temp;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        printMotionEvent(event);
        calculationDy(event);

        return false;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        printNewState(newState);
        this.newState = newState;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        Log.i(TAG, "onScrolled: dx = " + dx + " ; dy = " + dy);

        if (previousDataSize != getItemCount()) {

            // 判断最后一条可见条目的position == getItemCount()-1 ???
            int lVIPosition = layoutManager.findLastVisibleItemPosition();
//        Log.i(TAG, "onScrolled: lVIPosition = "+lVIPosition);
            if (lVIPosition == getItemCount() - 1) {// 总数据量占不满全屏， 不要 show footerView了
                isFullOccupied = false;
            } else {
                isFullOccupied = true;
            }
            previousDataSize = getItemCount();
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

                break;
            default:
                break;

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

        if (isFullOccupied) {
            int lCVIPosition = layoutManager.findLastCompletelyVisibleItemPosition();
//                    Log.i(TAG, "onDraggingLis: "+lCVIPosition);

            if (lCVIPosition == layoutManager.getItemCount() - 1) {
                if (footerItem != null && footerItem.getMeasuredHeight() == 0) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        if ((dy < -Dp2px.dp2px(10, recyclerView.getContext()))) {
                            showFooterItem();

                        }
                    }
                }
            }
        }
    }

    private class InnerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItem;

        public InnerViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }


}


