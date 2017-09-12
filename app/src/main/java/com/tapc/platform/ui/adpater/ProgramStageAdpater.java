package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.ProgramStage;
import com.tapc.platform.ui.view.Histogram;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ProgramStageAdpater extends BaseRecyclerViewAdapter<ProgramStageAdpater.ProgramStageViewHolder,
        ProgramStage> {

    public ProgramStageAdpater(List<ProgramStage> datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_program_stage;
    }

    @Override
    ProgramStageViewHolder getViewHolder(View view) {
        return new ProgramStageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgramStageViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ProgramStage item = mDatas.get(position);
        holder.itemView.setTag(item);
        holder.index.setText(String.valueOf(item.getIndex()));
        holder.time.setText(String.valueOf(item.getTime()) + " min");
        holder.reftValue.setMaxHeight(item.getMaxLeftValue());
        holder.rightValue.setMaxHeight(item.getMaxRightValue());
        holder.reftValue.setShowHeight(item.getLeftValue());
        holder.rightValue.setShowHeight(item.getRightValue());
    }

    public class ProgramStageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.program_stage_reftvalue)
        Histogram reftValue;
        @BindView(R.id.program_stage_rightvalue)
        Histogram rightValue;
        @BindView(R.id.program_stage_index)
        TextView index;
        @BindView(R.id.program_stage_time)
        TextView time;

        public ProgramStageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
