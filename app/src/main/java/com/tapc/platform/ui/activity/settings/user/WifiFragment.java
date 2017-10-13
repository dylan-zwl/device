package com.tapc.platform.ui.activity.settings.user;

import android.net.wifi.ScanResult;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tapc.platform.R;
import com.tapc.platform.model.wifi.WifiAdmin;
import com.tapc.platform.ui.adpater.WifiAdpater;
import com.tapc.platform.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/10.
 */

public class WifiFragment extends BaseFragment {
    @BindView(R.id.wifi_recyclerview)
    RecyclerView mRecyclerview;

    private WifiAdmin mWifiAdmin;
    private WifiAdpater mWifiAdpater;

    @Override
    protected int getContentView() {
        return R.layout.fragment_wifi;
    }

    @Override
    protected void initView() {
        super.initView();

        mWifiAdmin = new WifiAdmin(mContext);
        mWifiAdmin.openWifi();
        mWifiAdmin.startScan();

        List<String> list = new ArrayList<>();
        for (ScanResult result : mWifiAdmin.getWifiList()) {
            list.add(result.SSID);
        }
        mWifiAdpater = new WifiAdpater(list);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerview.setAdapter(mWifiAdpater);
        mWifiAdpater.notifyDataSetChanged();

        int netId = mWifiAdmin.AddWifiConfig(mWifiAdmin.getWifiList(), mWifiAdmin.getWifiList().get(0).SSID,
                "1681681681");
        if (netId != -1) {
            mWifiAdmin.getConfiguration();
            boolean ss = mWifiAdmin.ConnectWifi(netId);
        }
    }
}
