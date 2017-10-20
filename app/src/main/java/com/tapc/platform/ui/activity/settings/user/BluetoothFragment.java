package com.tapc.platform.ui.activity.settings.user;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.tapc.platform.R;
import com.tapc.platform.model.bluetooth.BluetoothModel;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.BluetoothAdpater;
import com.tapc.platform.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/10.
 */

public class BluetoothFragment extends BaseFragment implements BluetoothModel.Listener, BaseRecyclerViewAdapter
        .OnItemClickListener<BluetoothDevice> {
    @BindView(R.id.bluetooth_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.bluetooth_loading)
    ProgressBar mLoading;
    @BindView(R.id.bluetooth_enable)
    ToggleButton mEnableTBtn;

    private BluetoothAdpater mAdpater;
    private BluetoothModel mBluetoothModel;
    private List<BluetoothDevice> mList;
    private Handler mHandler;

    @Override
    protected int getContentView() {
        return R.layout.fragment_bluetooth;
    }

    @Override
    protected void initView() {
        super.initView();
        mHandler = new Handler();
        mList = new ArrayList<>();
        mAdpater = new BluetoothAdpater(mList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerview.setAdapter(mAdpater);
        mAdpater.setOnItemClickListener(this);
        mAdpater.notifyDataSetChanged();

        mBluetoothModel = new BluetoothModel(mContext);
        mBluetoothModel.setOnLitener(this);
        mBluetoothModel.start();
        mEnableTBtn.setChecked(mBluetoothModel.isEnabled());
    }

    @OnCheckedChanged(R.id.bluetooth_enable)
    void bluetoothEnableChange(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mBluetoothModel.enable();
            if (mBluetoothModel.isEnabled()) {
                mBluetoothModel.startDiscovery();
            } else {
                mEnableTBtn.setChecked(false);
            }
        } else {
            mBluetoothModel.disable();
        }
    }

    @OnClick(R.id.bluetooth_refresh)
    void refresh() {
        mList.clear();
        mAdpater.notifyDataSetChanged();
//        mBluetoothModel.cancelDiscovery();
        mBluetoothModel.startDiscovery();
    }

    @Override
    public void actionFound(BluetoothDevice bluetoothDevice) {
        mList.add(bluetoothDevice);
        mAdpater.notifyDataSetChanged();
    }

    @Override
    public void actionBondStateChanged(BluetoothDevice bluetoothDevice) {
        refreshList(bluetoothDevice);
    }

    @Override
    public void actionAclConnected(BluetoothDevice bluetoothDevice) {
        refreshList(bluetoothDevice);
    }

    @Override
    public void actionAclDisconnected(BluetoothDevice bluetoothDevice) {
        refreshList(bluetoothDevice);
    }

    @Override
    public void actionDiscoveryStarted() {
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void actionDiscoveryFinished() {
        mBluetoothModel.cancelDiscovery();
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, BluetoothDevice bluetoothDevice) {
        mBluetoothModel.doPair(bluetoothDevice);
    }

    private void refreshList(final BluetoothDevice bluetoothDevice) {
        mList.set(mList.indexOf(bluetoothDevice), bluetoothDevice);
        mAdpater.notifyDataSetChanged();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }
}
