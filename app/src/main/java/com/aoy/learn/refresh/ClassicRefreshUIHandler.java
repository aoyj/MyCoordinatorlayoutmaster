package com.aoy.learn.refresh;

/**
 * Created by drizzt on 2018/4/28.
 */

public interface ClassicRefreshUIHandler {
    void onPrograssChange(int p);
    void onStartRefresh();
    void onComplete();
}
