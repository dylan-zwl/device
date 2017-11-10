package com.tapc.platform.ui.activity.settings.system;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ImageView;

import com.tapc.platform.R;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.controller.MachineOperateController;
import com.tapc.platform.model.common.ConfigModel;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.SettingDevInfor;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.QrcodeUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/20.
 */

public class DevInforFragment extends BaseFragment {
    @BindView(R.id.dev_infor_id)
    SettingDevInfor mDeviceId;
    @BindView(R.id.dev_infor_app)
    SettingDevInfor mApp;
    @BindView(R.id.dev_infor_mcu)
    SettingDevInfor mMcu;
    @BindView(R.id.dev_infor_id_qr)
    ImageView mDevIdQr;

    private Handler mHandler;

    @Override
    protected int getContentView() {
        return R.layout.fragment_dev_infor;
    }

    @Override
    protected void initView() {
        super.initView();
        mHandler = new Handler();
        initAppVersionName();
        initMcuVersionName();
        initDeviceId();
    }

    private void initAppVersionName() {
        String version = AppUtils.getVersionName(mContext, mContext.getPackageName());
        mApp.setInfor(version);
    }

    private void initMcuVersionName() {
        MachineController.getInstance().getMcuVersion(new MachineOperateController.ResultListener() {
            @Override
            public void successful(final String s) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMcu.setInfor(s);
                    }
                });
            }

            @Override
            public void data(byte[] bytes, int i) {

            }
        });
    }

    private void initDeviceId() {
        mDeviceId.setInforSize(getResources().getDimension(R.dimen.tx_size2));

        final String id = ConfigModel.getDeviceId(mContext, "");
        if (TextUtils.isEmpty(id)) {
            return;
        }
        mDeviceId.setInfor(id);
        QrcodeUtils.show(id, mDevIdQr, 10, bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    @OnClick(R.id.setting_reset)
    void enterReset() {
        startActivity(new Intent(Settings.ACTION_PRIVACY_SETTINGS));
    }

    @OnClick(R.id.setting_uninstall)
    void uninstall() {
        AppUtils.unInstallApp(mContext, mContext.getPackageName());
    }
}
