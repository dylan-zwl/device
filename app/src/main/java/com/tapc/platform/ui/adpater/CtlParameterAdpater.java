package com.tapc.platform.ui.adpater;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.entity.CtlParameterItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/20.
 */

public class CtlParameterAdpater extends BaseRecyclerViewAdapter<CtlParameterAdpater.CtlParameterViewHolder,
        CtlParameterItem> {

    public CtlParameterAdpater(List<CtlParameterItem> datas) {
        super(datas);
    }

    @Override
    int getContentView() {
        return R.layout.view_setting_parameter;
    }

    @Override
    CtlParameterViewHolder getViewHolder(View view) {
        return new CtlParameterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CtlParameterViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final CtlParameterItem item = mDatas.get(position);
        String name = item.getName();
        if (name != null) {
            holder.name.setText(name);
        }
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.setValue(holder.value.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});


    }

    public class CtlParameterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.setting_parameter_name)
        TextView name;
        @BindView(R.id.setting_parameter_et)
        EditText value;

        public CtlParameterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
