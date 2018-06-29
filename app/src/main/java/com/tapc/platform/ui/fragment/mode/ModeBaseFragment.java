package com.tapc.platform.ui.fragment.mode;

import com.tapc.platform.ui.activity.start.StartActivity.StartActivityListener;
import com.tapc.platform.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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

    protected List<Object> getDefaultValues(Object... objects) {
        List<Object> defValues = new ArrayList<>();
        if (objects != null) {
            for (Object object : objects) {
                defValues.add(object);
            }
        }
        return defValues;
    }
}
