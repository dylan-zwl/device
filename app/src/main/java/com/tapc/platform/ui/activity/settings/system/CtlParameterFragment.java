package com.tapc.platform.ui.activity.settings.system;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.CtlParameterItem;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.controller.MachineOperateController;
import com.tapc.platform.library.uart.Utility;
import com.tapc.platform.model.common.CalibrationModel;
import com.tapc.platform.ui.adpater.CtlParameterAdpater;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.SettingFunctionBtn;
import com.tapc.platform.ui.widget.AlertDialog;
import com.tapc.platform.ui.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/20.
 */

public class CtlParameterFragment extends BaseFragment {
    @BindView(R.id.ctl_parameter_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.setting_calibration_incline)
    SettingFunctionBtn mInclineBtn;

    private CtlParameterAdpater mAdapter;
    private List<CtlParameterItem> mList;
    private CalibrationModel mCalibrationModel;
    private MachineController mMachineController;
    private Handler mHandler;
    private LoadingDialog mLoadingDialog;

    @Override
    protected int getContentView() {
        return R.layout.fragment_ctl_parameter;
    }

    @Override
    protected void initView() {
        super.initView();
        mHandler = new Handler();
        mMachineController = MachineController.getInstance();
        initCalibration();
        initParameter();
    }

    private void initCalibration() {
        mCalibrationModel = new CalibrationModel(mMachineController);
        mInclineBtn.setBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(mContext).setMsgText("请确定设备是否支持?").setOnClickListener(new AlertDialog.Listener() {
                    @Override
                    public void positiveOnCick() {
                        startCalibration();
                    }

                    @Override
                    public void negativeOnClick() {

                    }
                }).show();
            }
        });
    }

    private void startCalibration() {
        mLoadingDialog = new LoadingDialog(mContext).setMsgText("开始校准");
        mLoadingDialog.start();
        mCalibrationModel.startLeftCal(new MachineOperateController.ResultListener() {
            @Override
            public void successful(String s) {

            }

            @Override
            public void data(byte[] bytes, int i) {
                int value = Utility.getIntegerFromByteArray(bytes);
                if (mCalibrationModel.checkCalCompleted(value)) {
                    mLoadingDialog.setResult(true);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingDialog.stop();
                        }
                    }, 3000);
                }
            }
        });
    }

    private void initParameter() {
        mList = new ArrayList<>();
        mAdapter = new CtlParameterAdpater(mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        startGetMachineParam();
    }

    private void startGetMachineParam() {
        mMachineController.getMachineParam(new MachineOperateController.ResultListener() {
            @Override
            public void successful(String s) {

            }

            @Override
            public void data(byte[] bytes, int i) {
                for (int index = 0; index < 3; index++) {
                    CtlParameterItem item = new CtlParameterItem("参数" + index, "");
                    mList.add(item);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged(mList);
                    }
                });
            }
        });
    }

    @OnClick(R.id.ctl_parameter_save)
    void save() {
        byte[] data = new byte[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            data[i] = Byte.valueOf(mList.get(i).getValue());
        }
        mMachineController.setMachineParam(data);
        new AlertDialog(mContext).setMsgText("保存成功!").setButtonVisibility(View.GONE).setTimeOut(3000).show();
    }
}
