package com.tapc.platform.ui.fragment.mode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.db.IntervalEntity;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.PragramRunItem;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.library.util.WorkoutEnum.ProgramType;
import com.tapc.platform.model.program.ProgramModel;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.ProgramAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/4.
 */

public class ProgramFragment extends ModeBaseFragment {
    @BindView(R.id.mode_recyclerview)
    RecyclerView mRecyclerview;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mode;
    }

    @Override
    protected void initView() {
        List<PragramRunItem> list = new ArrayList<>();
        ProgramModel programModel = new ProgramModel(mContext, "program.db", "TAPC_PROG");
        String program = "program";
        List<List<IntervalEntity>> lists = programModel.getProgramList(program);
        if (lists != null) {
            int length = lists.size();
            for (int i = 0; i < length; i++) {
                PragramRunItem item = new PragramRunItem();
                item.setName(program + (i + 1));
                item.setProgram(lists.get(i));
                list.add(item);
            }
        }

        boolean isHasCustom = false;
        if (isHasCustom) {
            PragramRunItem customItem = new PragramRunItem();
            customItem.setName("自定义");
            customItem.setType(PragramRunItem.Type.ADD_PROGRAM);
            list.add(customItem);
        }

        ProgramAdapter programAdapter = new ProgramAdapter(list);

        programAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<PragramRunItem>() {
            @Override
            public void onItemClick(View view, PragramRunItem pragramRunItem) {
                if (mListener != null) {
                    if (pragramRunItem.getType() == PragramRunItem.Type.ADD_PROGRAM) {
                        mListener.switchProgramStageFragment(mContext);
                        return;
                    }
                    List<ParameterSet> list = new ArrayList<>();
                    list.add(new ParameterSet(getString(R.string.time), "30", getString(R.string.min_unit),
                            getDefaultValues(30, 60, 90), new ParameterSet.Range(5, 120)));
                    list.add(new ParameterSet(getString(R.string.weight), "60", getString(R.string.weight_unit),
                            getDefaultValues(60, 80, 100), new ParameterSet.Range(20, 300)));
                    ProgramType.TAPC_PROG.setName(pragramRunItem.getName());
                    mListener.switchParameterSettingsFragment(mContext, list, RunType.NOMAL, ProgramType.TAPC_PROG);
                }
            }
        });

        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerview.setAdapter(programAdapter);
        programAdapter.notifyDataSetChanged();
    }
}
