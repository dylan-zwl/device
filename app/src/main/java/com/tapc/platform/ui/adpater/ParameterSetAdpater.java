package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.entity.ParameterSet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ParameterSetAdpater extends BaseRecyclerViewAdapter<ParameterSetAdpater.ParameterViewHolder,
        ParameterSet> {

    public ParameterSetAdpater(List<ParameterSet> datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.item_parameter_settings;
    }

    @Override
    ParameterViewHolder getViewHolder(View view) {
        return new ParameterViewHolder(view);
    }

    @Override
    public ParameterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ParameterViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ParameterSet item = mDatas.get(position);
        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(this);
        String name = item.getName();
        if (name != null) {
            holder.name.setText(name);
        }
        String value = item.getValue();
        if (value != null) {
            holder.value.setText(value);
        }
        String unit = item.getUnit();
        if (unit != null) {
            holder.unit.setText(unit);
        }
        List<Object> defValues = item.getDefValues();
        if (defValues != null) {
            for (int i = 0; i < defValues.size(); i++) {
                switch (i) {
                    case 0:
                        String defValue1 = String.valueOf(defValues.get(i));
                        if (defValue1 != null) {
                            holder.defValueBtn1.setText(defValue1);
                        }
                        break;
                    case 1:
                        String defValue2 = String.valueOf(defValues.get(i));
                        if (defValue2 != null) {
                            holder.defValueBtn2.setText(defValue2);
                        }
                        break;
                    case 2:
                        String defValue3 = String.valueOf(defValues.get(i));
                        if (defValue3 != null) {
                            holder.defValueBtn3.setText(defValue3);
                        }
                        break;
                }
            }
        }
    }

    public class ParameterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parameter_settings_name)
        TextView name;
        @BindView(R.id.parameter_settings_value)
        TextView value;
        @BindView(R.id.parameter_settings_unit)
        TextView unit;
        @BindView(R.id.parameter_settings_defvalue_1)
        Button defValueBtn1;
        @BindView(R.id.parameter_settings_defvalue_2)
        Button defValueBtn2;
        @BindView(R.id.parameter_settings_defvalue_3)
        Button defValueBtn3;

        public ParameterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
