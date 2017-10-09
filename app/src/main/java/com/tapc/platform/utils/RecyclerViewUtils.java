package com.tapc.platform.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/9/28.
 */

public class RecyclerViewUtils {

    /**
     * 目标项是否在最后一个可见项之后
     */
    private boolean mShouldScroll;
    /**
     * 记录目标项位置
     */
    private int mToPosition;

    public int getFistItem(RecyclerView recyclerView) {
        return recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
    }

    public int getLasetItem(RecyclerView recyclerView) {
        return recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));
    }

    /**
     * 滑动到指定位置
     *
     * @param recyclerView
     * @param position
     */
    public void smoothMoveToPosition(RecyclerView recyclerView, final int position) {
        // 第一个可见位置
        int firstItem = getFistItem(recyclerView);
        // 最后一个可见位置
        int lastItem = getLasetItem(recyclerView);
        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            recyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < recyclerView.getChildCount()) {
                int top = recyclerView.getChildAt(movePosition).getTop();
                recyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            recyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }
}
