package com.gaos.app2.interfaces;

/**
 * Author:　Created by benjamin
 * DATE :  2017/9/29 15:58
 * versionCode:　v2.2
 */

public class IPullToRefreshManager {

    public interface IPullToRefresh {
        void onRefresh();

        void onLoadMore();
    }

    public enum EPullToRefreshResult {
        SUCCESSED,
        FAILED
    }

    public static IPullToRefreshResult iPullToRefreshResult;

    public interface IPullToRefreshResult {

        void onRefreshResult(EPullToRefreshResult ePullToRefreshResult);

        void onloadmoreResult(EPullToRefreshResult ePullToRefreshResult);
    }

    public static void setiPullToRefreshResult(IPullToRefreshResult iPullToRefreshResult) {
        IPullToRefreshManager.iPullToRefreshResult = iPullToRefreshResult;
    }

    public interface IFooterViewManager {
        void showEmpty();

        void showNone();

        void showloading();
    }

    public static IFooterViewManager iFooterViewManager;

    public static void setiFooterViewManager(IFooterViewManager iFooterViewManager) {
        IPullToRefreshManager.iFooterViewManager = iFooterViewManager;
    }
}
