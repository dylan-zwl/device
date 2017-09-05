package com.tapc.platform.ui.fragment.selectmode;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tapc.platform.R;
import com.tapc.platform.ui.adpater.ProgramAdpater;
import com.tapc.platform.ui.entity.PragramRunItem;
import com.tapc.platform.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/4.
 */

public class ProgramFragment extends BaseFragment {
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
        list.add(item);
        ProgramAdpater programAdpater = new ProgramAdpater(list);
        mRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerview.setAdapter(programAdpater);
        programAdpater.notifyDataSetChanged();
    }
}
