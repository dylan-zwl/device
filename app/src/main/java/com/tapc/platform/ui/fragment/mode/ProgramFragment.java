package com.tapc.platform.ui.fragment.mode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.entity.PragramRunItem;
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
        List<PragramRunItem> list = new ArrayList<PragramRunItem>();
        PragramRunItem item = new PragramRunItem();
        item.setName("山地跑");
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        PragramRunItem item2 = new PragramRunItem();
        item2.setName("自定义");
        item2.setType(PragramRunItem.Type.ADD_PROGRAM);
        list.add(item2);
        ProgramAdpater programAdpater = new ProgramAdpater(list);

        programAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<PragramRunItem>() {
            @Override
            public void onItemClick(View view, PragramRunItem pragramRunItem) {
                if (mListener != null) {
                    List<ParameterSet> list = new ArrayList<ParameterSet>();
                    List<Object> defValues = new ArrayList<Object>();
                    defValues.add("10");
                    defValues.add("20");
                    defValues.add("30");
                    list.add(new ParameterSet("时间", "30", "min", defValues));
                    list.add(new ParameterSet("体重", "6", "kg", defValues));
                    list.add(new ParameterSet("速度", "3.0", "km/h", defValues));
                    list.add(new ParameterSet("坡度", "6", "%", defValues));
                    mListener.switchParameterSettingsFragment(mContext, list);
                }
            }
        });

        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerview.setAdapter(programAdpater);
        programAdpater.notifyDataSetChanged();
    }
}
