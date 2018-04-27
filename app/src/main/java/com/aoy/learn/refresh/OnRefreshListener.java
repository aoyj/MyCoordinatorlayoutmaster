package com.aoy.learn.refresh;

/**
 * Created by drizzt on 2018/4/27.
 */

public interface OnRefreshListener {
    void onPullProgress(float progress);
    void startRefresh();
}
