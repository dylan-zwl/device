package com.tapc.platform.ui.adpater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class BaseAppAdpater<T> extends BaseAdapter {
    private List<T> mList;

    public BaseAppAdpater(List<T> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return (mList != null) ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void notifyDataSetChanged(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
