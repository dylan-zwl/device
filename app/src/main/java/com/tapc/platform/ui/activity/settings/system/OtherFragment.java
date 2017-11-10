package com.tapc.platform.ui.activity.settings.system;

import android.widget.CompoundButton;

import com.tapc.platform.R;
import com.tapc.platform.model.common.ConfigModel;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.SettingFunctionTbtn;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/20.
 */

public class OtherFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.setting_erp)
    SettingFunctionTbtn mErp;
    @BindView(R.id.setting_unmanned_run_check)
    SettingFunctionTbtn mUmannedCheck;
    @BindView(R.id.setting_rfid)
    SettingFunctionTbtn mRfid;

    @Override
    protected int getContentView() {
        return R.layout.fragment_setting_other;
    }

    @Override
    protected void initView() {
        super.initView();
        mErp.setOnCheckedChange(this);
        mUmannedCheck.setOnCheckedChange(this);
        mRfid.setOnCheckedChange(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        switch (button.getId()) {
            case R.id.setting_erp:
                ConfigModel.setErpFunction(mContext, isChecked);
                break;
            case R.id.setting_unmanned_run_check:
                ConfigModel.setUnmannedRunCheck(mContext, isChecked);
                break;
            case R.id.setting_rfid:
                ConfigModel.setRfidFunction(mContext, isChecked);
                break;
        }
    }
}
