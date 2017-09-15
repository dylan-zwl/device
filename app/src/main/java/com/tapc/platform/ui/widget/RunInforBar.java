package com.tapc.platform.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tapc.platform.R;
import com.tapc.platform.entity.RunInforBarItem;
import com.tapc.platform.ui.adpater.RunInforAdpater;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/28.
 */

public class RunInforBar extends BaseView {
    @BindView(R.id.run_infor_lv)
    RecyclerView mRecyclerView;
    @BindView(R.id.run_infor_bg)
    LinearLayout mBg;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;
    private RunInforAdpater mRunInforAdpater;

    public RunInforBar(Context context, WindowManager windowManager, WindowManager.LayoutParams windowManagerParams) {
        super(context);
        mWindowManager = windowManager;
        mWindowManagerParams = windowManagerParams;
    }

    @Override
    protected int getContentView() {
        return R.layout.widget_run_infor;
    }

    @Override
    protected void initView() {
        super.initView();
        List<RunInforBarItem> items = new ArrayList<>();
        items.add(new RunInforBarItem(R.drawable.ic_bar_distance, 0, "km"));
        items.add(new RunInforBarItem(R.drawable.ic_bar_caloria, 0, "kcal"));
        items.add(new RunInforBarItem(R.drawable.ic_bar_time, "00:00:00", ""));
        items.add(new RunInforBarItem(R.drawable.ic_bar_speed, 0, "km/h"));
        items.add(new RunInforBarItem(R.drawable.ic_bar_heart, 0, "kpm"));
        mRunInforAdpater = new RunInforAdpater(items);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRunInforAdpater);
    }

//    @OnClick(R.id.run_infor_chx)
//    void runInforOnClick(View v) {
//        IntentUtils.startActivity(mContext, RunInforActivity.class, null, Intent.FLAG_ACTIVITY_NEW_TASK | Intent
//                .FLAG_ACTIVITY_CLEAR_TOP);
//    }

//    @OnCheckedChanged(R.id.run_infor_chx)
//    void runInforChx(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            mBg.setBackgroundResource(R.drawable.bg_run_infor_bar);
//            mRecyclerView.setVisibility(VISIBLE);
//        } else {
//            mBg.setBackground(null);
//            mRecyclerView.setVisibility(GONE);
//        }
//    }

    // 刷新显示
    private void updateViewLayout(int x) {
        mWindowManagerParams.x = x;
        mWindowManager.updateViewLayout(this, mWindowManagerParams);
    }
}
