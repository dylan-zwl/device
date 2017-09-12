package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tapc.platform.R;
import com.tapc.platform.model.glide.GlideRoundTransform;
import com.tapc.platform.model.vaplayer.PlayEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/25.
 */

public class VaAdpater extends BaseRecyclerViewAdapter<VaAdpater.VaViewHolder, PlayEntity> implements View
        .OnClickListener {

    public VaAdpater(List<PlayEntity> list) {
        super(list);
    }

    @Override
    int getContentView() {
        return R.layout.item_va_mode;
    }

    @Override
    VaViewHolder getViewHolder(View view) {
        return new VaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VaViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        PlayEntity item = mDatas.get(position);
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(this);
        Glide.with(mContext).load(item.getPath() + "/" + item.getStill()).skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.NONE).transform(new GlideRoundTransform(mContext, 12)).into(holder.icon);
        String name = item.getName();
        if (!TextUtils.isEmpty(name)) {
            holder.name.setText(name);
        }
    }

    public class VaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.va_mode_icon)
        ImageView icon;
        @BindView(R.id.va_mode_name)
        TextView name;

        public VaViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
