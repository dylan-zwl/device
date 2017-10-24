package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.AppSettingItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/20.
 */

public class SettingAppAdapater extends BaseRecyclerViewAdapter<SettingAppAdapater.InstallViewHolder, AppSettingItem> {

    public class Status {
        public static final int NO_INSTALL = 0;
        public static final int INSTALLED = 1;
        public static final int INSTALLING = 2;
        public static final int INSTALL_FAILED = 3;
        public static final int INSTALL_SUCCESSED = 4;
        public static final int NO_SHOW = 0xff;
    }

    public SettingAppAdapater(List<AppSettingItem> datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_setting_app;
    }

    @Override
    InstallViewHolder getViewHolder(View view) {
        return new InstallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InstallViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final AppSettingItem item = mDatas.get(position);
        String name = item.getLabel();
        if (name != null) {
            holder.name.setText(name);
        }
        switch (item.getStatus()) {
            case Status.NO_INSTALL:
                holder.statusTx.setText("没有安装");
                break;
            case Status.INSTALLED:
                holder.statusTx.setText("已安装");
                break;
            case Status.INSTALLING:
                holder.statusTx.setText("安装中...");
                break;
            case Status.INSTALL_FAILED:
                holder.statusTx.setText("安装失败");
                break;
            case Status.INSTALL_SUCCESSED:
                holder.statusTx.setText("安装成功");
                break;
            case Status.NO_SHOW:
                holder.statusTx.setVisibility(View.GONE);
                break;
        }
        holder.icon.setBackground(item.getIcon());
        holder.chx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
            }
        });
        holder.installBtn.setTag(item);
        holder.installBtn.setOnClickListener(this);
    }

    public class InstallViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.setting_app_chx)
        CheckBox chx;
        @BindView(R.id.setting_app_ic)
        ImageView icon;
        @BindView(R.id.setting_app_name)
        TextView name;
        @BindView(R.id.setting_app_status)
        TextView statusTx;
        @BindView(R.id.setting_app_btn)
        Button installBtn;

        public InstallViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
