package com.tapc.platform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tapc.platform.R;
import com.tapc.platform.ui.widget.BaseView;
import com.tapc.platform.utils.FormatUtils;
import com.tapc.platform.utils.RxjavaUtils;

import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/8/28.
 */

public class DeviceCtl extends BaseView implements View.OnTouchListener {
    @BindView(R.id.device_ctl_icon)
    Button mIconBtn;
    @BindView(R.id.device_ctl_value)
    TextView mValue;
    @BindView(R.id.device_ctl_unit)
    TextView mUnit;

    @BindView(R.id.device_ctl_add)
    Button mCtlAdd;
    @BindView(R.id.device_ctl_sub)
    Button mCtlSub;

    private float mNowValue;
    private float mSetValuef;
    private Listener mListener;
    private Disposable mDisposable;
    private float mStepValue;
    private float mMaxValue;
    private float mMinValue;
    private float mDefValue;
    private int mIcon;
    private int mIconPressedId = -1;

    private enum ValueChange {
        ADD,
        SUB,
    }

    @Override
    protected int getContentView() {
        return R.layout.view_device_ctl;
    }

    public DeviceCtl(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DeviceCtl);
        mIcon = array.getResourceId(R.styleable.DeviceCtl_ctlIcon, -1);
        if (mIcon != -1) {
            mIconBtn.setBackgroundResource(mIcon);
        }
        String unit = array.getString(R.styleable.DeviceCtl_ctlUnit);
        if (!TextUtils.isEmpty(unit)) {
            mUnit.setText(unit);
        }
        array.recycle();
        mCtlAdd.setOnTouchListener(this);
        mCtlSub.setOnTouchListener(this);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    public double getValue() {
        return mNowValue;
    }

    public void setValue(float value) {
        mSetValuef = FormatUtils.formatFloat(1, value, RoundingMode.HALF_UP);
        this.mNowValue = mSetValuef;
        mValue.setText(formatShowStr(mSetValuef));
    }

    public String formatShowStr(float value) {
        return String.format("%.1f", value);
    }

    @OnClick(R.id.device_ctl_add)
    void onAddClick(View v) {
        if (mListener != null) {
            mListener.onAddClick();
        }
    }

    @OnClick(R.id.device_ctl_sub)
    void onSubClick(View v) {
        if (mListener != null) {
            mListener.onSubClick();
        }
    }

    @OnLongClick(R.id.device_ctl_add)
    boolean onAddLongClick(View v) {
        startObserable(ValueChange.ADD);
        return true;
    }

    @OnLongClick(R.id.device_ctl_sub)
    boolean onSubLongClick(View v) {
        startObserable(ValueChange.SUB);
        return true;
    }

    public void cancelObserable() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mDisposable = null;
            if (mListener != null) {
                mListener.setDeviceValue(mSetValuef);
            }
        }
    }

    void startObserable(final ValueChange valueChange) {
        cancelObserable();
        mDisposable = RxjavaUtils.interval(150, 150, TimeUnit.MILLISECONDS, new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                switch (valueChange) {
                    case ADD:
                        mSetValuef = mSetValuef + mStepValue;
                        if (mSetValuef > mMaxValue) {
                            mSetValuef = mMaxValue;
                        }
                        mValue.setText(formatShowStr(mSetValuef));
                        break;
                    case SUB:
                        mSetValuef = mSetValuef - mStepValue;
                        if (mSetValuef < mMinValue) {
                            mSetValuef = mMinValue;
                        }
                        mValue.setText(formatShowStr(mSetValuef));
                        break;
                }
            }
        }, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                cancelObserable();
                break;
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return false;
    }

    public void setOnClickListener(@NonNull Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        void onAddClick();

        void onSubClick();

        void setDeviceValue(float value);

        void onCtlTypeClick(int icon);
    }

    public void setConfig(float minValue, float maxValue, float stepValue, float defValue) {
        this.mMinValue = minValue;
        this.mMaxValue = maxValue;
        this.mStepValue = stepValue;
        this.mDefValue = defValue;
        setValue(defValue);
    }

    public void setPause() {
        mCtlAdd.setBackgroundResource(R.drawable.btn_add_p);
        mCtlSub.setBackgroundResource(R.drawable.btn_sub_p);
        if (mIconPressedId != -1) {
            mIconBtn.setBackgroundResource(mIconPressedId);
        }
        mCtlAdd.setEnabled(false);
        mCtlSub.setEnabled(false);
        mIconBtn.setEnabled(false);
    }

    public void setResume() {
        mCtlAdd.setBackgroundResource(R.drawable.btn_add);
        mCtlSub.setBackgroundResource(R.drawable.btn_sub);
        mIconBtn.setBackgroundResource(mIcon);
        mCtlAdd.setEnabled(true);
        mCtlSub.setEnabled(true);
        mIconBtn.setEnabled(true);
    }

    public void setIconPressedResource(int id) {
        mIconPressedId = id;
    }

    @OnClick(R.id.device_ctl_icon)
    void onCtlType() {
        if (mListener != null) {
            mListener.onCtlTypeClick(mIcon);
        }
    }

    public int getIcon() {
        return mIcon;
    }

    public float getMinValue() {
        return mMinValue;
    }

    public float getMaxValue() {
        return mMaxValue;
    }
}
