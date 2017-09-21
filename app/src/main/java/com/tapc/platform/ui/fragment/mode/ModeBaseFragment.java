package com.tapc.platform.ui.fragment.mode;

import com.tapc.platform.ui.activity.start.StartActivity.StartActivityListener;
import com.tapc.platform.ui.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/9/20.
 */

public class ModeBaseFragment extends BaseFragment {
    protected StartActivityListener mListener;

    @Override
    protected int getContentView() {
        return 0;
    }

    protected void setListener(StartActivityListener listener) {
        mListener = listener;
    }
}
