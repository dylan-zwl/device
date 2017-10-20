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

public class WifiAdpater extends BaseRecyclerViewAdapter<WifiAdpater.WifiViewHolder, ConnectStatusItem> {

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
