package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.tapc.platform.R;
import com.tapc.platform.ui.entity.AppInfoEntity;
import com.tapc.platform.ui.view.RoundRectImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/29.
 */

public class AppAdpater extends BaseRecyclerViewAdapter<AppAdpater.AppViewHolder, AppInfoEntity> {
    public AppAdpater(List datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_app;
    }

    @Override
    AppViewHolder getViewHolder(View view) {
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        AppInfoEntity item = mDatas.get(position);
        holder.appClick.setTag(item);
        holder.appClick.setOnClickListener(this);
//        Glide.with(mContext).load(item.getAppIcon()).skipMemoryCache(true).diskCacheStrategy
//                (DiskCacheStrategy.NONE).transform(new GlideRoundTransform(mContext, 12)).into(holder.icon);
        holder.icon.setImageDrawable(item.getAppIcon());
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.app_icon)
        RoundRectImageView icon;
        @BindView(R.id.app_click)
        ImageButton appClick;


        public AppViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
