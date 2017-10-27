package com.tapc.platform.ui.activity.settings.system;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.ImageView;

import com.tapc.platform.R;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.controller.MachineOperateController;
import com.tapc.platform.model.ConfigModel;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.SettingDevInfor;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.QrcodeUtils;
import com.tapc.platform.utils.RxjavaUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
        final String id = ConfigModel.getDeviceId(mContext, "");
        mDeviceId.setInfor(id);
        RxjavaUtils.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                SystemClock.sleep(500);
                e.onNext(QrcodeUtils.createImage(id, mDevIdQr.getWidth(), mDevIdQr.getHeight(), 10));
            }
        }, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Drawable drawable = (Drawable) o;
                if (drawable != null) {
                    mDevIdQr.setBackground(drawable);
                }
            }
        }, bindUntilEvent(FragmentEvent.DESTROY_VIEW));
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
