package com.tapc.platform.ui.activity.settings.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.tapc.platform.R;
import com.tapc.platform.entity.ConnectStatusItem;
import com.tapc.platform.model.wifi.WifiAdmin;
import com.tapc.platform.model.wifi.WifiPassard;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.WifiAdpater;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/10.
 */

public class WifiFragment extends BaseFragment {
    @BindView(R.id.setting_wifi_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.setting_wifi_loading)
    ProgressBar mLoading;
    @BindView(R.id.setting_wifi_enable)
    ToggleButton mEnableTBtn;
    @BindView(R.id.setting_wifi_item_ll)
    LinearLayout mItemLL;
    @BindView(R.id.setting_wifi_pwd_ll)
    LinearLayout mPwdLL;
    @BindView(R.id.setting_wifi_cancel_save)
    Button mConnectSaveBtn;
    @BindView(R.id.setting_wifi_connect)
    Button mConnectBtn;
    @BindView(R.id.setting_wifi_pwd_edit)
    EditText mPwdEdit;

    private WifiAdmin mWifiAdmin;
    private WifiAdpater mAdpater;
    private Handler mHandler;
    private List<ConnectStatusItem> mShowList;
    private ConnectStatusItem mConnectStatusItem;

    @Override
    protected int getContentView() {
        return R.layout.fragment_wifi;
    }

    @Override
    protected void initView() {
        super.initView();
        mHandler = new Handler();
        IntentUtils.registerReceiver(mContext, mNetWorkBroadcastReceiver, ConnectivityManager.CONNECTIVITY_ACTION,
                WifiManager.RSSI_CHANGED_ACTION);

        mWifiAdmin = new WifiAdmin(mContext);
        mShowList = new ArrayList<>();
        mAdpater = new WifiAdpater(mShowList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerview.setAdapter(mAdpater);
        mAdpater.notifyDataSetChanged();

        mAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ConnectStatusItem>() {
            @Override
            public void onItemClick(View view, ConnectStatusItem connectStatusItem) {
                mWifiAdmin.getConfiguration();
                int networkId = mWifiAdmin.isConfiguration(connectStatusItem.getSSID());
                if (networkId < 0) {
                    mConnectSaveBtn.setVisibility(View.GONE);
                } else {
                    mConnectSaveBtn.setVisibility(View.VISIBLE);
                }
                if (connectStatusItem.getConnectedStatus() == 1) {
                    mConnectBtn.setText("取消连接");
                } else {
                    mConnectBtn.setText("连接");
                }
                mConnectStatusItem = connectStatusItem;
                mConnectStatusItem.setNetworkId(networkId);
                setPwdVisibility(true);
            }
        });

        mEnableTBtn.setChecked(mWifiAdmin.isWifiEnabled());
    }

    @OnCheckedChanged(R.id.setting_wifi_enable)
    void wifiEnableChange(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mWifiAdmin.openWifi();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mWifiAdmin.isWifiEnabled()) {
                        mRecyclerview.setVisibility(View.VISIBLE);
                        refreshShowList();
                        return;
                    } else {
                        mEnableTBtn.setChecked(false);
                    }
                }
            }, 3000);
        } else {
            mWifiAdmin.closeWifi();
        }
        mRecyclerview.setVisibility(View.GONE);
    }

    @OnClick(R.id.setting_wifi_cancel_save)
    void cancelSave() {
        mConnectStatusItem.setConnectedStatus(0);
        mWifiAdmin.removeNetwork(mConnectStatusItem.getNetworkId());
        setPwdVisibility(false);
    }

    @OnClick(R.id.setting_wifi_connect)
    void connectOnClick() {
        String name = mConnectBtn.getText().toString();
        if (name.equals("连接")) {
            String pwd = mPwdEdit.getEditableText().toString();
            if (TextUtils.isEmpty(pwd)) {
                return;
            }
            int networkId = mWifiAdmin.addWifiConfig(mWifiAdmin.getWifiList().get(0).SSID, pwd, mConnectStatusItem
                    .getPwdType());
            mWifiAdmin.getConfiguration();
            boolean result = mWifiAdmin.connectWifi(networkId);
            for (ConnectStatusItem item : mShowList) {
                item.setConnectedStatus(0);
            }
            if (result) {
                mConnectStatusItem.setConnectedStatus(1);
            } else {
                mConnectStatusItem.setConnectedStatus(2);
            }
            mAdpater.notifyDataSetChanged();
        } else {
            mConnectStatusItem.setConnectedStatus(0);
            mWifiAdmin.disConnectionWifi(mConnectStatusItem.getNetworkId());
        }
        setPwdVisibility(false);
    }

    @OnClick(R.id.setting_wifi_cancel)
    void cancelOnClick() {
        setPwdVisibility(false);
    }


    @OnClick(R.id.setting_wifi_refresh)
    void refresh() {
        refreshShowList();
    }

    private void refreshShowList() {
        mShowList.clear();
//        List<WifiConfiguration> configurationList = mWifiAdmin.getConfiguration();
//        if (configurationList != null) {
//            for (WifiConfiguration configuration : configurationList) {
//                ConnectStatusItem item = new ConnectStatusItem();
//                item.setSSID(configuration.SSID);
//                item.setConnectedStatus(0);
//                item.setLevel(item.getLevel());
//                mShowList.add(item);
//            }
//        }
        mWifiAdmin.startScan();
        List<ScanResult> scanResultList = mWifiAdmin.getWifiList();
        if (scanResultList != null) {
            String connectedBSSID = mWifiAdmin.getConnectedBSSID();
            for (ScanResult result : scanResultList) {
                ConnectStatusItem item = new ConnectStatusItem();
                item.setSSID(result.SSID);
                if (result.BSSID.equals(connectedBSSID)) {
                    item.setConnectedStatus(1);
                } else {
                    item.setConnectedStatus(0);
                }
                item.setPwdType(WifiPassard.getSecurity(result));
                item.setBSSID(connectedBSSID);
                item.setLevel(WifiManager.calculateSignalLevel(result.level, 5));
                mShowList.add(item);
            }
        }
        mAdpater.notifyDataSetChanged();
    }


    public BroadcastReceiver mNetWorkBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    break;
                case WifiManager.RSSI_CHANGED_ACTION:
                    break;
            }
            if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
//                showNetWork(context);
            }
        }
    };

    private void setPwdVisibility(boolean visibility) {
        if (visibility) {
            mItemLL.setVisibility(View.INVISIBLE);
            mPwdLL.setVisibility(View.VISIBLE);
            mItemLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_out));
            mPwdLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_in));
        } else {
            mItemLL.setVisibility(View.VISIBLE);
            mPwdLL.setVisibility(View.INVISIBLE);
            mPwdEdit.setText("");
            mItemLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
            mPwdLL.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_out));
        }
    }
}
