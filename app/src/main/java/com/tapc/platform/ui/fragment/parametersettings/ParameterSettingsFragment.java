package com.tapc.platform.ui.fragment.parametersettings;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tapc.platform.R;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.library.abstractset.ProgramSetting;
import com.tapc.platform.library.common.TreadmillSystemSettings;
import com.tapc.platform.library.data.TreadmillProgramSetting;
import com.tapc.platform.library.util.WorkoutEnum.ProgramType;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.ParameterSetAdapter;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.KeyboardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ParameterSettingsFragment extends BaseFragment {
    @BindView(R.id.paramenter_settings_recyclerview)
    RecyclerView mRecyclerview;

    private ParameterSetAdapter mAdpater;

    private ProgramType mProgramType = ProgramType.NORMAL;
    private List<ParameterSet> mDataList;
    private String mCurrentShowName;
    private PopupWindow mWindow;
    private TreadmillProgramSetting mTreadmillProgramSetting;
    private ProgramSetting mProgramSetting;

    @Override
    protected int getContentView() {
        return R.layout.fragment_parameter_settings;
    }

    public void setDataList(List<ParameterSet> dataList) {
        this.mDataList = dataList;
    }

    public void init(ProgramType programType) {
        mProgramType = programType;
    }

    @Override
    protected void initView() {
        super.initView();
        if (mDataList == null) {
            mDataList = new ArrayList<ParameterSet>();
        }
        mAdpater = new ParameterSetAdapter(mDataList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, mDataList.size());
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        mRecyclerview.setAdapter(mAdpater);
        mAdpater.notifyDataSetChanged();
        mAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ParameterSet>() {
            @Override
            public void onItemClick(View view, final ParameterSet parameterSet) {
                if (parameterSet.getName() == mCurrentShowName) {
                    return;
                }
                mCurrentShowName = parameterSet.getName();
                KeyboardView popupView = new KeyboardView(mContext);
                cancelPopupWindow();
                mWindow = new PopupWindow(popupView, view.getWidth(), WindowManager.LayoutParams
                        .WRAP_CONTENT);
                mWindow.setFocusable(false);
                mWindow.setAnimationStyle(R.style.popup_window_anim);
                mWindow.setOutsideTouchable(true);
                mWindow.update();
                mWindow.showAsDropDown(view, 0, 20, Gravity.CENTER);
                popupView.setValue("", 6);
                popupView.setListener(new KeyboardView.KeyboardListener() {
                    @Override
                    public void onValueChange(String value) {
                        parameterSet.setValue(value);
                        mAdpater.notifyDataSetChanged();
                    }
                });
            }
        });

        mAdpater.setDefValueListener(new ParameterSetAdapter.DefValueListener() {
            @Override
            public void onDefValueBtnClick(Object value, int position) {
                String showValue = String.valueOf(value);
                ParameterSettingsFragment.this.mDataList.get(position).setValue(showValue);
                mAdpater.notifyDataSetChanged();
            }
        });
    }

    public TreadmillProgramSetting getmProgramSetting() {
        if (mProgramType == null) {
            mProgramType = ProgramType.NORMAL;
        }
        TreadmillProgramSetting programSetting = new TreadmillProgramSetting(mProgramType);
        programSetting.setSpeed(TreadmillSystemSettings.MIN_SPEED);
        programSetting.setIncline(TreadmillSystemSettings.MIN_INCLINE);
        for (ParameterSet item : mDataList) {
            float value = Float.valueOf(item.getValue());
            String name = item.getName();
            if (name.equals(getString(R.string.time))) {
                mProgramType.setGoal(value * 60);
            } else if (name.equals(getString(R.string.distance)) || name.equals(getString(R.string.calorie))) {
                mProgramType.setGoal(value);
            } else if (name.equals(getString(R.string.speed))) {
                programSetting.setSpeed(value);
            } else if (name.equals(getString(R.string.incline))) {
                programSetting.setIncline(value);
            }
        }
        return programSetting;
    }

    private void cancelPopupWindow() {
        if (mWindow != null && mWindow.isShowing()) {
            mWindow.dismiss();
            mWindow = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelPopupWindow();
    }
}
