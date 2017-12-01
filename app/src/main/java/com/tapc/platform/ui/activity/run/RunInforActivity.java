package com.tapc.platform.ui.activity.run;

import android.view.View;

import com.tapc.platform.R;
import com.tapc.platform.ui.activity.BaseActivity;
import com.tapc.platform.ui.view.TopTitle;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/5.
 */

public class RunInforActivity extends BaseActivity {
    @BindView(R.id.run_infor_title)
    TopTitle mTopTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_run_infor;
    }

    @Override
    protected void initView() {
        mTopTitle.setShowBack(true);
        mTopTitle.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
