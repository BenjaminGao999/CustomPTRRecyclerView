package com.adnonstop.combinationptrrv.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adnonstop.combinationptrrv.R;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewLoadMoreDataResult;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewOnTouchListener;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewScrollListener;
import com.adnonstop.combinationptrrv.interfaces.IRecyclerViewLoadMoreData;

import java.util.ArrayList;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/14 9:50
 * versionCode:　v2.2
 */
public class MyAdapter extends RecyclerView.Adapter implements IRecyclerViewScrollListener, IRecyclerViewOnTouchListener, IRecyclerViewLoadMoreDataResult {
    private static final String TAG = "MyAdapter";
    private ArrayList<String> data;
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
    private FrameLayout mFLFooterContainer;
    private View footerViewLoading;
    private IRecyclerViewLoadMoreData iRecyclerViewLoadMoreData;
    private View footerViewNoMoreData;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_items_layout, parent, false);
            return new InnerViewHolder(itemView);
        } else {

            initFooterViewContainer(parent);
            return new FooterViewHolder(mFLFooterContainer);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM) {
            final InnerViewHolder holder1 = (InnerViewHolder) holder;
            holder1.tvItem.setText(data.get(position));

            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(holder1.itemView.getContext(), "" + data.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (getItemViewType(position) == FOOTER) {

        }

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

        return false;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        printNewState(newState);
        this.newState = newState;

        if (newState == RecyclerView.SCROLL_STATE_IDLE && isFullOccupied) {
            // 判断最后一条可见条目的position == getItemCount()-1 ???
            int lVIPosition = layoutManager.findLastVisibleItemPosition();
//        Log.i(TAG, "onScrolled: lVIPosition = "+lVIPosition);
            if (lVIPosition == getItemCount() - 1) {// 加载更多数据
                if (iRecyclerViewLoadMoreData != null) {
                    iRecyclerViewLoadMoreData.loadMoreData();
                }


            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        Log.i(TAG, "onScrolled: dx = " + dx + " ; dy = " + dy);

        if (previousDataSize != getItemCount()) {

            // 判断最后一条可见条目的position == getItemCount()-1 ???
            int lVIPosition = layoutManager.findLastVisibleItemPosition();
//        Log.i(TAG, "onScrolled: lVIPosition = "+lVIPosition);

            initFooterViewContainer(recyclerView);
            if (lVIPosition == getItemCount() - 1) {// 总数据量占不满全屏， 不要 show footerView了
                mFLFooterContainer.removeAllViews();
                isFullOccupied = false;

            } else {
                mFLFooterContainer.removeAllViews();
                initFooterViewLoading(mFLFooterContainer);
                mFLFooterContainer.addView(footerViewLoading);
                isFullOccupied = true;

            }

            previousDataSize = getItemCount();
        }
    }


    public void setIRecyclerViewLoadMoreData(IRecyclerViewLoadMoreData iRecyclerViewLoadMoreData) {
        this.iRecyclerViewLoadMoreData = iRecyclerViewLoadMoreData;
    }


    private void initFooterViewContainer(ViewGroup parent) {
        if (mFLFooterContainer == null) {
            mFLFooterContainer = new FrameLayout(parent.getContext());
            mFLFooterContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void initFooterViewLoading(FrameLayout mFLFooterContainer) {
        if (footerViewLoading == null) {
            footerViewLoading = LayoutInflater.from(mFLFooterContainer.getContext()).inflate(R.layout.ptr_footer_loading_layout, mFLFooterContainer, false);
            footerViewLoading.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void initFooterViewNoMoreData() {
        mFLFooterContainer.removeAllViews();
        if (footerViewNoMoreData == null) {
            footerViewNoMoreData = LayoutInflater.from(mFLFooterContainer.getContext()).inflate(R.layout.ptr_footer_no_more_data_layout, mFLFooterContainer, false);
            footerViewNoMoreData.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public void onLoadMoreDataResult(Object data) {
        if (data != null) {// 添加数据，并刷新


        } else {
            initFooterViewContainer(recyclerView);
            initFooterViewNoMoreData();
            mFLFooterContainer.removeAllViews();
            mFLFooterContainer.addView(footerViewNoMoreData);
        }
    }


    private class InnerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItem;

        public InnerViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }


    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }
}


