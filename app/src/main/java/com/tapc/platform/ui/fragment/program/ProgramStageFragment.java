package com.tapc.platform.ui.fragment.program;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tapc.platform.R;
import com.tapc.platform.entity.ProgramStage;
import com.tapc.platform.ui.adpater.ProgramStageAdpater;
import com.tapc.platform.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ProgramStageFragment extends BaseFragment {
    @BindView(R.id.program_stage_recyclerview)
    RecyclerView mRecyclerview;

    private ProgramStageAdpater mAdpater;

    @Override
    protected int getContentView() {
        return R.layout.fragment_program_stage;
    }

    @Override
    protected void initView() {
        super.initView();
        List<ProgramStage> list = new ArrayList<ProgramStage>();
        for (int i = 1; i <= 16; i++) {
            ProgramStage item = new ProgramStage();
            item.setIndex(i);
            item.setLeftValue(i);
            item.setRightValue(i);
            item.setMaxLeftValue(16);
            item.setMaxRightValue(20);
            item.setTime(3);
            list.add(item);
        }
        ProgramStageAdpater programAdpater = new ProgramStageAdpater(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setAdapter(programAdpater);
        programAdpater.notifyDataSetChanged();
    }
}
