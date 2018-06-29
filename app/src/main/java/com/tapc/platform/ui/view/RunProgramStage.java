package com.tapc.platform.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class RunProgramStage extends BaseView {
    @BindView(R.id.run_program_current_value)
    TextView mCurrentValue;
    @BindView(R.id.run_program_differences)
    TextView mDifferences;
    @BindView(R.id.run_program_title)
    TextView mTitle;
    @BindView(R.id.program_chart)
    ProgramChart mProgramChart;

    private float mProgramValue;

    public RunProgramStage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContentView() {
        return R.layout.run_program_stage;
    }

    public void setCurrentValue(float currentValue) {
        float temp = currentValue - mProgramValue;
        if (temp == 0) {
            mDifferences.setText("");
        } else {
            mDifferences.setText(String.format("%.1f", temp));
        }
    }

    public void setProgramValue(float programValue) {
        mProgramValue = programValue;
        mCurrentValue.setText(String.valueOf(programValue));
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setProgramChart(List<Float> blockList, List<Float> lineList) {
        int row = blockList == null ? 0 : blockList.size();
        int column = lineList == null ? 0 : lineList.size();
        mProgramChart.init(row, column);
        mProgramChart.setBlockList(blockList);
        mProgramChart.setLineList(null);
        mProgramChart.invalidate();
    }
}
