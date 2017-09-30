package com.gaos.app2;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Author:　Created by benjamin
 * DATE :  2017/9/28 20:54
 * versionCode:　v2.2
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<String> data;
    private static final String TAG = "MyRecyclerViewAdapter";
    private static final int ITEM_TYPE_FOOTER_VIEW = 100;
    private static final int ITEM_TYPE_ITEM_NORMAL = 101;

    public MyRecyclerViewAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view, parent, false);
            CommonRecyclerViewVH vh = new CommonRecyclerViewVH(view);
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

                    Toast.makeText(myViewHolder.itemView.getContext(), "" + data.get(position), Toast.LENGTH_SHORT).show();
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
//        return super.getItemViewType(position);
//        Log.e(TAG, "getItemViewType: position = " + position);
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
