package com.tapc.platform.ui.activity.settings.system;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.tapc.platform.R;
import com.tapc.platform.entity.AppSettingItem;
import com.tapc.platform.model.uninstall.UninstallModel;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.SettingAppAdapater;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.widget.AlertDialog;
import com.tapc.platform.ui.widget.LoadingDialog;
import com.tapc.platform.utils.RxjavaUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/20.
 */

public class UninstallFragment extends BaseFragment {
    @BindView(R.id.app_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.app_btn)
    Button mAllBtn;

    private SettingAppAdapater mAdapter;
    private List<AppSettingItem> mShowList = new ArrayList<>();
    private UninstallModel mModel;
    private LoadingDialog mLoadingDialog;

    @Override
    protected int getContentView() {
        return R.layout.fragment_setting_app;
    }

    @Override
    protected void initView() {
        super.initView();

        mAllBtn.setText("一键卸载");

        RxjavaUtils.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                mModel = new UninstallModel(mContext);
                mShowList = mModel.getFiles();
                if (mShowList != null && mShowList.size() > 0) {
                    e.onNext("show");
                }
            }
        }, new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                mAdapter = new SettingAppAdapater(mShowList, SettingAppAdapater.UNINSTALL);
                mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<AppSettingItem>() {
                    @Override
                    public void onItemClick(View view, AppSettingItem item) {
                        List<AppSettingItem> list = new ArrayList<>();
                        list.add(item);
                        uninstallApp(list);
                    }
                });
                mAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog(mContext).setMsgText("是否确定?").setOnClickListener(new AlertDialog.Listener() {
                            @Override
                            public void positiveOnCick() {
                                List<AppSettingItem> list = new ArrayList<>();
                                for (AppSettingItem item : mShowList) {
                                    if (item.isChecked()) {
                                        list.add(item);
                                    }
                                }
                                uninstallApp(list);
                            }

                            @Override
                            public void negativeOnClick() {

                            }
                        }).show();
                    }
                });

                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mModel.setListener(new UninstallModel.Listener() {
                    @Override
                    public void completed(AppSettingItem item, String s, int i) {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, this.bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    private void uninstallApp(final List<AppSettingItem> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mLoadingDialog = new LoadingDialog(mContext).setMsgText("开始校准");
                mLoadingDialog.start();
                for (AppSettingItem item : list) {
                    mModel.uninstallApp(item);
                }
                mLoadingDialog.stop();
            }
        }).start();
    }


    @OnCheckedChanged({R.id.app_all_select, R.id.app_system_show})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setting_app_btn:

                break;
            case R.id.setting_app_chx:
                for (AppSettingItem item : mShowList) {
                    item.setChecked(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
