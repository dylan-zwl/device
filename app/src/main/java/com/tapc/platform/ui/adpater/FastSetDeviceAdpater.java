package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tapc.platform.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/6.
 */

public class FastSetDeviceAdpater extends BaseRecyclerViewAdapter<FastSetDeviceAdpater.FastSetDeviceViewHolder,
        String> {

    public FastSetDeviceAdpater(List datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_fase_set_ctl_value;
    }

    @Override
    FastSetDeviceViewHolder getViewHolder(View view) {
        return new FastSetDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FastSetDeviceViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String valueStr = mDatas.get(position);
        holder.itemView.setTag(valueStr);
        holder.itemView.setOnClickListener(this);
        holder.value.setText(valueStr);
    }

    public class FastSetDeviceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ctl_value)
        Button value;

        public FastSetDeviceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
