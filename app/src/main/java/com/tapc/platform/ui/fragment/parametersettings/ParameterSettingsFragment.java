package com.tapc.platform.ui.fragment.parametersettings;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tapc.platform.R;
import com.tapc.platform.ui.adpater.BaseRecyclerViewAdapter;
import com.tapc.platform.ui.adpater.ParameterSetAdpater;
import com.tapc.platform.entity.ParameterSet;
import com.tapc.platform.ui.fragment.BaseFragment;
import com.tapc.platform.ui.view.KeyboardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ParameterSettingsFragment extends BaseFragment {
    @BindView(R.id.paramenter_settings_recyclerview)
    RecyclerView mRecyclerview;

    private ParameterSetAdpater mAdpater;
    private List<ParameterSet> mDataList;
    private String mCurrentShowName;
    private PopupWindow mWindow;

    @Override
    protected int getContentView() {
        return R.layout.fragment_parameter_settings;
    }

    @Override
    protected void initView() {
        super.initView();
        List<ParameterSet> list = getParameterList();
        mAdpater = new ParameterSetAdpater(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        mRecyclerview.setAdapter(mAdpater);
        mAdpater.notifyDataSetChanged();
        mAdpater.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ParameterSet>() {
            @Override
            public void onItemClick(View view, final ParameterSet parameterSet) {
                if (parameterSet.getName() == mCurrentShowName) {
                    return;
                }
                mCurrentShowName = parameterSet.getName();
                KeyboardView popupView = new KeyboardView(mContext);
                if (mWindow != null && mWindow.isShowing()) {
                    mWindow.dismiss();
                    mWindow = null;
                }
                mWindow = new PopupWindow(popupView, view.getWidth(), WindowManager.LayoutParams
                        .WRAP_CONTENT);
                mWindow.setFocusable(false);
                mWindow.setAnimationStyle(R.style.popup_window_anim);
                mWindow.setOutsideTouchable(true);
                mWindow.update();
                mWindow.showAsDropDown(view, 0, 20, Gravity.CENTER);
                popupView.setValue("", 6);
                popupView.setListener(new KeyboardView.KeyboardListener() {
                    @Override
                    public void onValueChange(String value) {
                        parameterSet.setValue(value);
                        mAdpater.notifyDataSetChanged();
                    }
                });
            }
        });

        mAdpater.setDefValueListener(new ParameterSetAdpater.DefValueListener() {
            @Override
            public void onDefValueBtnClick(Object value, int position) {
                String showValue = String.valueOf(value);
                mDataList.get(position).setValue(showValue);
                mAdpater.notifyDataSetChanged();
            }
        });
    }

    private List<ParameterSet> getParameterList() {
        List<ParameterSet> list = new ArrayList<ParameterSet>();
        List<Object> defValues = new ArrayList<Object>();
        defValues.add("10");
        defValues.add("20");
        defValues.add("30");
        list.add(new ParameterSet("距离", "30", "km", defValues));
        list.add(new ParameterSet("初始速度", "3.0", "km/h", defValues));
        list.add(new ParameterSet("初始坡度", "6", "%", defValues));
        return list;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
