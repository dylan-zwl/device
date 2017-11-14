package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.GoalModeItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/25.
 */

public class GoalModeAdapter extends BaseRecyclerViewAdapter<GoalModeAdapter.GoalViewHolder, GoalModeItem> {

    public GoalModeAdapter(List<GoalModeItem> list) {
        super(list);
    }

    @Override
    int getContentView() {
        return R.layout.item_goal_mode;
    }

    @Override
    GoalViewHolder getViewHolder(View view) {
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        GoalModeItem item = mDatas.get(position);
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(this);
        holder.icon.setBackgroundResource(item.getIcon());
        String nameStr = mContext.getResources().getString(item.getName());
        if (nameStr != null) {
            holder.name.setText(nameStr);
        }
        if (item.isChecked()) {
            holder.bg.setBackgroundResource(R.drawable.bg_goal_mode_item);
        } else {
            holder.bg.setBackgroundDrawable(null);
        }
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goal_mode_icon)
        ImageView icon;
        @BindView(R.id.goal_mode_name)
        TextView name;
        @BindView(R.id.goal_mode_bg)
        LinearLayout bg;

        public GoalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
