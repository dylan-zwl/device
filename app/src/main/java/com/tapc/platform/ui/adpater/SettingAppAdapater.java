package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.model.app.AppSettingItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/20.
 */

public class SettingAppAdapater extends BaseRecyclerViewAdapter<SettingAppAdapater.SettingAppViewHolder,
        AppSettingItem> {
    public static final int INSTALL = 0;
    public static final int UNINSTALL = 1;
    private int mSettingType = INSTALL;

    public class Status {
        public static final int NO_SHOW = 0;
        public static final int NO_INSTALL = 1;
        public static final int INSTALLED = 2;
        public static final int INSTALLING = 3;
        public static final int INSTALL_FAILED = 4;
        public static final int INSTALL_SUCCESSED = 5;
    }

    public SettingAppAdapater(List<AppSettingItem> datas, int settingType) {
        super(datas);
        mSettingType = settingType;
    }

    @Override
    int getContentView() {
        return R.layout.item_setting_app;
    }

    @Override
    SettingAppViewHolder getViewHolder(View view) {
        return new SettingAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SettingAppViewHolder holder, int position) {
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
            default:
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
        holder.btn.setTag(item);
        switch (mSettingType) {
            case INSTALL:
                holder.btn.setText("安装");
                break;
            case UNINSTALL:
                holder.btn.setText("卸载");
                break;
        }
        holder.btn.setOnClickListener(this);
    }

    public class SettingAppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.setting_app_chx)
        CheckBox chx;
        @BindView(R.id.setting_app_ic)
        ImageView icon;
        @BindView(R.id.setting_app_name)
        TextView name;
        @BindView(R.id.setting_app_status)
        TextView statusTx;
        @BindView(R.id.setting_app_btn)
        Button btn;

        public SettingAppViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
