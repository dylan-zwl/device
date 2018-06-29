package com.tapc.platform.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;

import java.util.List;

import butterknife.BindView;

public class ProgramImagView extends BaseView {
    @BindView(R.id.program_chart)
    ProgramChart mProgramChart;
    @BindView(R.id.program_delete)
    Button mDeleteBtn;

    public ProgramImagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContentView() {
        return R.layout.view_program_imageview;
    }

    public void setDeleteOnClick(OnClickListener l) {
        mDeleteBtn.setOnClickListener(l);
    }

    public void setDeleteBtnVisibility(boolean visibility) {
        mDeleteBtn.setVisibility(GONE);
    }

    public void setProgramChart(List<Float> blockList, List<Float> lineList) {
        int row = blockList == null ? 0 : blockList.size();
        int column = lineList == null ? 0 : lineList.size();
        mProgramChart.init(row, column);
        mProgramChart.setBlockList(blockList);
        mProgramChart.setLineList(lineList);
        mProgramChart.invalidate();
    }
}
