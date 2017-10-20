package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.ConnectStatusItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class BluetoothAdpater extends BaseRecyclerViewAdapter<BluetoothAdpater.WifiViewHolder, ConnectStatusItem> {

    public BluetoothAdpater(List datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_bluetooth;
    }

    @Override
    WifiViewHolder getViewHolder(View view) {
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ConnectStatusItem item = mDatas.get(position);
        holder.itemView.setOnClickListener(this);
        if (item != null) {
            String nameStr = item.getName();
            if (nameStr != null) {
                holder.name.setText(nameStr);
            }
            if (item.isConnected()) {
                holder.status.setVisibility(View.VISIBLE);
            } else {
                holder.status.setVisibility(View.GONE);
            }
            int icId = 0;
            switch (item.getType()) {
                case 0:
                    icId = R.drawable.ic_blue_phone;
                    break;
                case 1:
                    icId = R.drawable.ic_blue_headset;
                    break;
            }
            if (icId != 0) {
                holder.type.setBackgroundResource(icId);
            }
        }
    }

    public class WifiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bluetooth_name)
        TextView name;
        @BindView(R.id.bluetooth_connect_status)
        TextView status;
        @BindView(R.id.bluetooth_type)
        ImageView type;

        public WifiViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
