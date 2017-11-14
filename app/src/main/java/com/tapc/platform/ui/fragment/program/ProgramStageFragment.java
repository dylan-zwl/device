package com.tapc.platform.ui.fragment.program;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.entity.ProgramStage;
import com.tapc.platform.library.common.TreadmillSystemSettings;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.ProgramStageAdapter;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.SetStageParameter;
import com.tapc.platform.utils.RecyclerViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ProgramStageFragment extends BaseFragment {
    @BindView(R.id.program_stage_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.program_stage_setting_ll)
    LinearLayout mSettingLL;
    @BindView(R.id.set_stage_parameter_time)
    SetStageParameter mSetTime;
    @BindView(R.id.set_stage_parameter_left)
    SetStageParameter mSetLeft;
    @BindView(R.id.set_stage_parameter_right)
    SetStageParameter mSetRight;

    private ProgramStageAdapter mProgramAdpater;
    private int mCurrentStage;
    private List<ProgramStage> mList;
    private RecyclerViewUtils mRecyclerViewUtils;

    private float minLeftValue;
    private float maxLeftValue;
    private float minRightValue;
    private float maxRightValue;

    @Override
    protected int getContentView() {
        return R.layout.fragment_program_stage;
    }

    @Override
    protected void initView() {
        super.initView();
        minLeftValue = TreadmillSystemSettings.MIN_INCLINE;
        maxLeftValue = TreadmillSystemSettings.MAX_INCLINE;
        minRightValue = TreadmillSystemSettings.MIN_SPEED;
        maxRightValue = TreadmillSystemSettings.MAX_SPEED;

        mList = new ArrayList<ProgramStage>();
        for (int i = 0; i < 16; i++) {
            ProgramStage item = new ProgramStage();
            item.setIndex(i);
            item.setLeftValue(i);
            item.setRightValue(i);
            item.setMaxLeftValue(maxLeftValue);
            item.setMaxRightValue(maxRightValue);
            item.setTime(5);
            mList.add(item);
        }
        mProgramAdpater = new ProgramStageAdapter(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setAdapter(mProgramAdpater);
        mProgramAdpater.notifyDataSetChanged();

        mProgramAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ProgramStage>() {
            @Override
            public void onItemClick(View view, ProgramStage programStage) {
                mCurrentStage = programStage.getIndex();
                ProgramStage item = mList.get(mCurrentStage);
                mSetTime.setValue(item.getTime());
                mSetLeft.setValue(item.getLeftValue());
                mSetRight.setValue(item.getRightValue());
                mSettingLL.setVisibility(View.VISIBLE);
                Log.d(mContext.getClass().getName(), "program stage : " + mCurrentStage);
            }
        });

        mSetTime.initShow(0, 60, 1, 0);
        mSetTime.setValueListener(new SetStageParameter.ValueListener() {
            @Override
            public void onChangeValue(float value) {
                setValue(SetType.TIME, value);
            }
        });

        mSetLeft.initShow(minLeftValue, maxLeftValue, 1, 1);
        mSetLeft.setValueListener(new SetStageParameter.ValueListener() {
            @Override
            public void onChangeValue(float value) {
                setValue(SetType.LEFT, value);
            }
        });

        mSetRight.initShow(minRightValue, maxRightValue, 0.1f, 1);
        mSetRight.setValueListener(new SetStageParameter.ValueListener() {
            @Override
            public void onChangeValue(float value) {
                setValue(SetType.RIGHT, value);
            }
        });
        mSettingLL.setVisibility(View.INVISIBLE);

        mRecyclerViewUtils = new RecyclerViewUtils();
    }

    private enum SetType {
        TIME,
        LEFT,
        RIGHT
    }

    private void setValue(SetType type, Float value) {
        ProgramStage item = mList.get(mCurrentStage);
        if (item == null) {
            return;
        }
        switch (type) {
            case TIME:
                item.setTime(value.intValue());
                break;
            case LEFT:
                item.setLeftValue(value);
                break;
            case RIGHT:
                item.setRightValue(value);
                break;
        }
        mProgramAdpater.notifyItemChanged(mCurrentStage);
    }

    @OnClick(R.id.program_stage_delete)
    void delete() {
        mList.remove(mCurrentStage);
        mSettingLL.setVisibility(View.INVISIBLE);
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setIndex(i);
        }
        mProgramAdpater.notifyDataSetChanged();
    }

    @OnClick(R.id.program_stage_create)
    void create() {
        ProgramStage item = new ProgramStage();
        item.setLeftValue(minLeftValue);
        item.setRightValue(minRightValue);
        item.setMaxLeftValue(maxLeftValue);
        item.setMaxRightValue(maxRightValue);
        item.setTime(5);
        mList.add(item);
        int lastIndex = mList.size() - 1;
        item.setIndex(lastIndex);
        mRecyclerViewUtils.smoothMoveToPosition(mRecyclerview, lastIndex);
        mProgramAdpater.notifyDataSetChanged();
    }

    @OnClick(R.id.program_stage_previous_btn)
    void previousShow(View v) {
        int fistItem = mRecyclerViewUtils.getFistItem(mRecyclerview);
        if (fistItem >= 1) {
            fistItem--;
        }
        mRecyclerViewUtils.smoothMoveToPosition(mRecyclerview, fistItem);
    }

    @OnClick(R.id.program_stage_next_btn)
    void nextShow(View v) {
        int lastItem = mRecyclerViewUtils.getLasetItem(mRecyclerview);
        if (lastItem <= (mList.size() - 1)) {
            lastItem++;
        }
        mRecyclerViewUtils.smoothMoveToPosition(mRecyclerview, lastItem);
    }
}
