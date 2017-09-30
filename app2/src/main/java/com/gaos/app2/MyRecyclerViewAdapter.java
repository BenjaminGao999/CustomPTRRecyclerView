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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demo, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tvItem.setText(data.get(position));
        myViewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(myViewHolder.itemView.getContext(), "" + data.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }


    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }

}
