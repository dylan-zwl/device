package com.tapc.platform.ui.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView
        .Adapter<VH> implements View.OnClickListener {
    protected Context mContext;
    protected List<T> mDatas;

    public BaseRecyclerViewAdapter(List<T> datas) {
        mDatas = datas;
    }

    abstract int getContentView();

    abstract VH getViewHolder(View view);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(getContentView(), parent, false);
        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (mDatas != null) ? mDatas.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            T t = (T) v.getTag();
            mOnItemClickListener.onItemClick(v, t);
        }
    }

    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> listhener) {
        mOnItemClickListener = listhener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T t);
    }

    public void notifyDataSetChanged(List<T> list) {
        mDatas = list;
        notifyDataSetChanged();
    }
}
