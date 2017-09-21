package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.PragramRunItem;
import com.tapc.platform.ui.view.ProgramImagView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/4.
 */

public class ProgramAdpater extends BaseRecyclerViewAdapter<ProgramAdpater.PragramViewHolder, PragramRunItem> {
    public ProgramAdpater(List datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_program;
    }

    @Override
    PragramViewHolder getViewHolder(View view) {
        return new PragramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PragramViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        PragramRunItem item = mDatas.get(position);
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(this);
        String name = item.getName();
        if (name != null) {
            holder.name.setText(name);
        }

        switch (item.getType()) {
            case COMMON:
                holder.image.setDeleteBtnVisibility(false);
                break;
            case USER_ADD:
                holder.image.setVisibility(View.GONE);
                break;
            case ADD_PROGRAM:
                holder.image.setVisibility(View.GONE);
                holder.addBtn.setVisibility(View.VISIBLE);
                break;
        }

        holder.image.setDeleteOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dd;
            }
        });

//        holder.image.setRunList(item.getList());
    }

    public class PragramViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.program_image)
        ProgramImagView image;
        @BindView(R.id.program_add)
        Button addBtn;
        @BindView(R.id.program_name)
        TextView name;

        public PragramViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
