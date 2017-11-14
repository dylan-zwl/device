package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.tapc.platform.R;
import com.tapc.platform.entity.AppInfoEntity;
import com.tapc.platform.ui.view.RoundRectImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/29.
 */

public class AppAdapter extends BaseRecyclerViewAdapter<AppAdapter.AppViewHolder, AppInfoEntity> {
    private int lastPosition = -1;

    public AppAdapter(List datas) {
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
//                (DiskCacheStrategy.NONE).transform(new GlideRoundTransform(mContext, 40)).into(holder.icon);
        holder.icon.setBackground(item.getAppIcon());
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(AppViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.app_icon)
        RoundRectImageView icon;
        //        @BindView(R.id.app_icon)
        //        ImageView icon;
        @BindView(R.id.app_click)
        ImageButton appClick;


        public AppViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
