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
 * Created by Administrator on 2017/8/28.
 */

public class RunInforAdpater extends BaseRecyclerViewAdapter<RunInforAdpater.RunInforViewHolder, WorkoutInforItem> {

    public RunInforAdpater(List datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_run_infor;
    }

    @Override
    RunInforViewHolder getViewHolder(View view) {
        return new RunInforViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RunInforViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        WorkoutInforItem item = mDatas.get(position);
        holder.icon.setBackgroundResource(item.getIcon());
        String value = item.getValue();
        if (value != null) {
            holder.value.setText(value);
        }
        holder.unit.setText(item.getUnit());
    }

    public class RunInforViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.run_infor_ic)
        ImageView icon;
        @BindView(R.id.run_infor_value)
        TextView value;
        @BindView(R.id.run_infor_unit)
        TextView unit;

        public RunInforViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
