package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.WorkoutInforItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5.
 */

public class WorkoutResultAdapter extends BaseRecyclerViewAdapter<WorkoutResultAdapter.WorkoutResultViewHolder,
        WorkoutInforItem> {

    public WorkoutResultAdapter(List datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.view_workout_result_infor;
    }

    @Override
    WorkoutResultViewHolder getViewHolder(View view) {
        return new WorkoutResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutResultViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        WorkoutInforItem item = mDatas.get(position);
        holder.icon.setImageResource(item.getIcon());
        holder.name.setText(item.getName());
        String value = item.getValue();
        if (value != null) {
            holder.value.setText(value);
        }
        holder.unit.setText(item.getUnit());
    }

    public class WorkoutResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.result_icon)
        ImageView icon;
        @BindView(R.id.result_value)
        TextView value;
        @BindView(R.id.result_unit)
        TextView unit;
        @BindView(R.id.result_name)
        TextView name;

        public WorkoutResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
