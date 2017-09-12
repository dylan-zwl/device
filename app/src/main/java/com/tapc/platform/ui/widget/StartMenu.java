package com.tapc.platform.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.entity.AppInfoEntity;
import com.tapc.platform.ui.adpater.AppAdpater;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.IntentUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/25.
 */

public class StartMenu extends BaseView {
    @BindView(R.id.menu_bar_rv)
    RecyclerView mRecyclerview;

    private AppAdpater mAppAdpater;

    @Override
    protected int getContentView() {
        return R.layout.widget_start_menu;
    }

    public StartMenu(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        ArrayList<AppInfoEntity> allAppInfo = AppUtils.getAllAppInfo(mContext);
        mAppAdpater = new AppAdpater(allAppInfo);

        mAppAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<AppInfoEntity>() {
            @Override
            public void onItemClick(View view, AppInfoEntity appInfoEntity) {
                mContext.startActivity(appInfoEntity.getIntent());
            }
        });
        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        mRecyclerview.setAdapter(mAppAdpater);
    }

    @OnClick(R.id.start_menu_back)
    void backOnClick() {
        TapcApplication.getInstance().getKeyEvent().backEvent();
    }

    @OnClick(R.id.start_menu_home)
    void homeOnClick() {
        IntentUtils.home(mContext);
        // TapcApp.getmInstance().keyboardEvent.homeEvent();
    }

}
