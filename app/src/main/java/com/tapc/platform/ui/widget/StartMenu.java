package com.tapc.platform.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.tapc.platform.R;
import com.tapc.platform.application.TapcApplication;
import com.tapc.platform.jni.Driver;
import com.tapc.platform.model.app.AppInfoEntity;
import com.tapc.platform.model.app.AppModel;
import com.tapc.platform.ui.adpater.AppAdapter;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.view.BaseSystemView;
import com.tapc.platform.utils.AppUtils;
import com.tapc.platform.utils.IntentUtils;
import com.tapc.platform.utils.WindowManagerUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/25.
 */

public class StartMenu extends BaseSystemView {
    @BindView(R.id.menu_bar_rv)
    RecyclerView mRecyclerview;

    private AppAdapter mAppAdapter;
    private Disposable mDisposable;

    @Override
    protected int getContentView() {
        return R.layout.widget_start_menu;
    }

    public StartMenu(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> s) throws Exception {
                ArrayList<AppInfoEntity> allAppInfo = AppModel.getAllAppInfo(mContext, false);
                mAppAdapter = new AppAdapter(allAppInfo);
                mAppAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<AppInfoEntity>() {
                    @Override
                    public void onItemClick(View view, AppInfoEntity appInfoEntity) {
                        mContext.startActivity(appInfoEntity.getIntent());
                    }
                });
                s.onNext("start_show");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager
                        .HORIZONTAL));
                mRecyclerview.setAdapter(mAppAdapter);
            }
        });
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return WindowManagerUtils.getLayoutParams(0, 0, 480, LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.RIGHT);
    }

    @OnClick(R.id.start_menu_back)
    void backOnClick() {
        Driver.back();
    }

    @OnClick(R.id.start_menu_home)
    void homeOnClick() {
        try {
            if (!AppUtils.isApplicationBroughtToBackground(mContext)) {
                IntentUtils.startActivity(mContext, TapcApplication.getInstance().getHomeActivity(), null, Intent
                        .FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        } catch (Exception e) {
            Log.d(this.toString(), e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
