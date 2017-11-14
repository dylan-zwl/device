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

public class WifiAdapter extends BaseRecyclerViewAdapter<WifiAdapter.WifiViewHolder, ConnectStatusItem> {

    public WifiAdapter(List datas) {
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
        ConnectStatusItem item = mDatas.get(position);
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(this);
        if (item != null) {
            String nameStr = item.getSSID();
            if (nameStr != null) {
                holder.name.setText(nameStr);
            }
            switch (item.getConnectedStatus()) {
                case 1:
                    holder.status.setText("已连接");
                    break;
                case 2:
                    holder.status.setText("连接失败");
                    break;
                case 0:
                default:
                    holder.status.setText("");
                    break;
            }
            int icId = 0;
            switch (item.getLevel()) {
                case 0:
                case 1:
                    icId = R.drawable.ic_wifi_1;
                    break;
                case 2:
                    icId = R.drawable.ic_wifi_2;
                    break;
                case 3:
                    icId = R.drawable.ic_wifi_3;
                    break;
                case 4:
                    icId = R.drawable.ic_wifi_4;
                    break;
            }
            if (icId != 0) {
                holder.statusIc.setBackgroundResource(icId);
            }
        }
    }

    public class WifiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.wifi_name)
        TextView name;
        @BindView(R.id.wifi_connect_status)
        TextView status;
        @BindView(R.id.wifi_connect_status_ic)
        ImageView statusIc;

        public WifiViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
