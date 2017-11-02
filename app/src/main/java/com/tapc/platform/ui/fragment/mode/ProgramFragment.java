package com.tapc.platform.ui.fragment.mode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.PragramRunItem;
import com.tapc.platform.entity.RunType;
import com.tapc.platform.library.util.WorkoutEnum.ProgramType;
import com.tapc.platform.library.workouting.WorkOuting;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.ProgramAdpater;

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
        ArrayList<String> programList = WorkOuting.getProgramName();
        List<PragramRunItem> list = new ArrayList<PragramRunItem>();
        if (programList != null) {
            for (String programItem : programList) {
                PragramRunItem item = new PragramRunItem();
                item.setName(programItem);
                list.add(item);
            }
        }
        PragramRunItem customItem = new PragramRunItem();
        customItem.setName("自定义");
        customItem.setType(PragramRunItem.Type.ADD_PROGRAM);
        list.add(customItem);

        ProgramAdpater programAdpater = new ProgramAdpater(list);

        programAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<PragramRunItem>() {
            @Override
            public void onItemClick(View view, PragramRunItem pragramRunItem) {
                if (mListener != null) {
                    if (pragramRunItem.getType() == PragramRunItem.Type.ADD_PROGRAM) {
                        mListener.switchProgramStageFragment(mContext);
                        return;
                    }
                    List<ParameterSet> list = new ArrayList<ParameterSet>();
                    List<Object> defValues = new ArrayList<Object>();
                    defValues.add("10");
                    defValues.add("20");
                    defValues.add("30");
                    list.add(new ParameterSet("时间", "30", "min", defValues));
                    list.add(new ParameterSet("体重", "60", "kg", defValues));
                    list.add(new ParameterSet("速度", "1.0", "km/h", defValues));
                    list.add(new ParameterSet("坡度", "0", "%", defValues));
                    mListener.switchParameterSettingsFragment(mContext, list, RunType.NOMAL, ProgramType.TIME);
                }
            }
        });

        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerview.setAdapter(programAdpater);
        programAdpater.notifyDataSetChanged();
    }
}
