package com.tapc.platform.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.tapc.platform.R;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.FastSetDeviceAdpater;
import com.tapc.platform.ui.widget.BaseView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/6.
 */

public class FastDeviceCtl extends BaseView {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.device_ctl_icon)
    Button mIcon;

    private List<String> mDataList;
    private FastSetDeviceAdpater mAdpater;
    private int mIconId;

    @Override
    protected int getContentView() {
        return R.layout.view_fase_set_device_ctl;
    }

    public FastDeviceCtl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        mAdpater = new FastSetDeviceAdpater(mDataList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerview.setLayoutManager(manager);
        mRecyclerview.setAdapter(mAdpater);
        mAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, String value) {
                if (mListener != null) {
                    mListener.onValueClick(value);
                    mListener.close();
                }
            }
        });
    }

    @OnClick(R.id.device_ctl_icon)
    void onTypeClick(View v) {
        if (mListener != null) {
            mListener.close();
        }
    }

    public int getIconId() {
        return mIconId;
    }

    public void setIcon(int id) {
        mIconId = id;
        mIcon.setBackgroundResource(id);
    }

    public void updateShow(List<String> list) {
        mDataList = list;
        mAdpater.notifyDataSetChanged(list);
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        void onValueClick(String value);

        void close();
    }
}
