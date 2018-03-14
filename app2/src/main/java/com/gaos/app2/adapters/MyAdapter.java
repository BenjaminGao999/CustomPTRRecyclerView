package com.gaos.app2.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaos.app2.CustomViews.FooterViewManager;
import com.gaos.app2.interfaces.IPullToRefreshManager;
import com.gaos.app2.R;

import java.util.ArrayList;

/**
 * Author:　Created by benjamin
 * DATE :  2017/9/28 20:54
 * versionCode:　v2.2
 */

public class MyAdapter extends RecyclerView.Adapter {

    private ArrayList<String> data;
    private static final String TAG = "MyAdapter";
    private static final int ITEM_TYPE_FOOTER_VIEW = 100;
    private static final int ITEM_TYPE_ITEM_NORMAL = 101;
    private FooterViewManager footerView;

    public MyAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER_VIEW) {

//            footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view_layout, parent, false);
            footerView = new FooterViewManager(parent.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(layoutParams);
            CommonRecyclerViewVH vh = new CommonRecyclerViewVH(footerView);
            return vh;
        } else {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo, parent, false);
            return new MyViewHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (getItemViewType(position) == ITEM_TYPE_FOOTER_VIEW) {
            // do nothing

        } else {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.tvItem.setText(data.get(position));
            myViewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Toast.makeText(myViewHolder.itemView.getContext(), "" + data.get(position), Toast.LENGTH_SHORT).show();
                    int layoutPosition = myViewHolder.getLayoutPosition();
                    Log.i(TAG, "onClick: layoutPosition = "+layoutPosition);

                    int adapterPosition = myViewHolder.getAdapterPosition();
                    Log.i(TAG, "onClick: adapterPosition = "+adapterPosition);

                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return ITEM_TYPE_FOOTER_VIEW;
        } else {
            return ITEM_TYPE_ITEM_NORMAL;
        }
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView != null) {
            LinearLayoutManager recyclerViewLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (recyclerViewLayoutManager != null) {
                IPullToRefreshManager.setiFooterViewManager(new IPullToRefreshManager.IFooterViewManager() {
                    @Override
                    public void showEmpty() {

                    }

                    @Override
                    public void showNone() {

                    }

                    @Override
                    public void showloading() {

                    }
                });
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }

}
