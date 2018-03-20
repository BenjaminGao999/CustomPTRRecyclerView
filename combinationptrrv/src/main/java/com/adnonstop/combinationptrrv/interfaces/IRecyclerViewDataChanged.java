package com.adnonstop.combinationptrrv.interfaces;

/**
 * Author:　Created by benjamin
 * DATE :  2018/3/20 17:22
 * versionCode:　v2.2
 * <p>
 * 提供刷新， 加载更多的接口
 */

public interface IRecyclerViewDataChanged<T> {

    void onDataRefreshed(T t);

    void onLoadMoreDataResult(T t);


}
