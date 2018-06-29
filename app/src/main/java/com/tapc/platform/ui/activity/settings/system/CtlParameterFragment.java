package com.tapc.platform.ui.activity.settings.system;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.CtlParameterItem;
import com.tapc.platform.library.controller.MachineController;
import com.tapc.platform.library.controller.MachineOperateController;
import com.tapc.platform.library.uart.Utility;
import com.tapc.platform.model.common.CalibrationModel;
import com.tapc.platform.ui.adpater.CtlParameterAdapter;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.SettingFunctionBtn;
import com.tapc.platform.ui.widget.AlertDialog;
import com.tapc.platform.ui.widget.LoadingDialog;
import com.tapc.platform.utils.RxjavaUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/20.
 */

public class CtlParameterFragment extends BaseFragment {
    @BindView(R.id.ctl_parameter_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.setting_calibration_incline)
    SettingFunctionBtn mCalibreationBtn;

    private CtlParameterAdapter mAdapter;
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

    /**
     * 校准
     */
    private void initCalibration() {
        mCalibrationModel = new CalibrationModel(mMachineController);
        mCalibreationBtn.setBtnOnClickListener(new View.OnClickListener() {
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
                    RxjavaUtils.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> e) throws Exception {
                            e.onNext("success");
                            SystemClock.sleep(3000);
                            e.onNext("stop");
                        }
                    }, new Consumer() {
                        @Override
                        public void accept(Object o) throws Exception {
                            switch ((String) o) {
                                case "success":
                                    mLoadingDialog.setResult(true);
                                    break;
                                case "stop":
                                    mLoadingDialog.stop();
                                    break;
                            }
                        }
                    }, bindUntilEvent(FragmentEvent.DESTROY_VIEW));
                }
            }
        });
    }

    /**
     * 参数设置
     */
    private void initParameter() {
        mList = new ArrayList<>();
        mAdapter = new CtlParameterAdapter(mList);
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
                mList.clear();
                for (int index = 0; index < bytes.length / 2; index++) {
                    if (bytes.length >= (index * 2 + 2)) {
                        int value = getDataInt(bytes, index * 2, 2);
                        CtlParameterItem item = new CtlParameterItem("参数" + index, 0);
                        item.setValue(value);
                        mList.add(item);
                    }
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

    private int getDataInt(byte[] datas, int start, int bits) {
        byte[] dataCache = new byte[2];
        System.arraycopy(datas, start, dataCache, 0, bits);
        return Utility.getIntegerFromByteArray(dataCache);
    }

    @OnClick(R.id.ctl_parameter_save)
    void save() {
        byte[] data = new byte[mList.size() * 2];
        for (int i = 0; i < mList.size(); i++) {
            byte[] temp = Utility.getByteArrayFromInteger(mList.get(i).getValue(), 2);
            System.arraycopy(temp, 0, data, i * 2, 2);
        }
        mMachineController.setMachineParam(data);
        new AlertDialog(mContext).setMsgText("已设置").setButtonVisibility(View.GONE).setTimeOut(3000).show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startGetMachineParam();
            }
        }, 500);
    }
}
