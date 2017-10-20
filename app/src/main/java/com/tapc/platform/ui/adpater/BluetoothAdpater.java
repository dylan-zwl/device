package com.tapc.platform.ui.adpater;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class BluetoothAdpater extends BaseRecyclerViewAdapter<BluetoothAdpater.WifiViewHolder, BluetoothDevice> {

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
        BluetoothDevice item = mDatas.get(position);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(item);
        if (item != null) {
            String nameStr = item.getName();
            if (nameStr != null) {
                holder.name.setText(nameStr);
            }
            if (item.getBondState() == BluetoothDevice.BOND_BONDED) {
                holder.status.setVisibility(View.VISIBLE);
            } else {
                holder.status.setVisibility(View.GONE);
            }
            int icId = 0;
            switch (item.getBluetoothClass().getMajorDeviceClass()) {
                case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                    icId = R.drawable.ic_blue_headset;
                    break;
                default:
                    icId = R.drawable.ic_blue_phone;
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
