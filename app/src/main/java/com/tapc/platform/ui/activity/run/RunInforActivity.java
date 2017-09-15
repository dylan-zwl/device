package com.tapc.platform.ui.activity.run;

import android.view.View;
import android.widget.Button;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/5.
 */

public class RunInforActivity extends BaseActivity {
    @BindView(R.id.title_back)
    Button mBackBtn;

    @Override
    protected int getContentView() {
        return R.layout.activity_run_infor;
    }

    @Override
    protected void initView() {
        mBackBtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @OnClick(R.id.title_back)
    void back() {
        finish();
    }
}
