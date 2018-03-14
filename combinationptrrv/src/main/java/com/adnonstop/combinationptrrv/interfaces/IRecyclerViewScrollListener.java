package com.adnonstop.combinationptrrv.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/14 10:27
 * versionCode:　v2.2
 */

public interface IRecyclerViewScrollListener {

     void onScrollStateChanged(RecyclerView recyclerView, int newState);


     void onScrolled(RecyclerView recyclerView, int dx, int dy);
}
