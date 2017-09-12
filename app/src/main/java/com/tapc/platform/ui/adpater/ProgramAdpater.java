package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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
        String name = item.getName();
        if (name != null) {
            holder.name.setText(name);
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
        @BindView(R.id.program_name)
        TextView name;

        public PragramViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
