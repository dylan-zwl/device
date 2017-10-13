package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tapc.platform.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class WifiAdpater extends BaseRecyclerViewAdapter<WifiAdpater.WifiViewHolder,
        String> {

    public WifiAdpater(List datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_wifi;
    }

    @Override
    WifiViewHolder getViewHolder(View view) {
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String item = mDatas.get(position);
        holder.itemView.setOnClickListener(this);
        if (item != null) {
            holder.name.setText(item);
        }
    }

    public class WifiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.wifi_name)
        TextView name;

        public WifiViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
