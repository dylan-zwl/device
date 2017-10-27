package com.tapc.platform.ui.activity.settings.user;

import android.support.annotation.IdRes;
import android.widget.RadioGroup;

import com.tapc.platform.R;
import com.tapc.platform.model.ConfigModel;
import com.tapc.platform.model.language.LanguageModel;
import com.tapc.platform.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/10.
 */

public class LanguageFragment extends BaseFragment {
    @BindView(R.id.language_group)
    RadioGroup mRadioGroup;

    private LanguageModel.Language mLanguage;

    @Override
    protected int getContentView() {
        return R.layout.fragment_language;
    }

    @Override
    protected void initView() {
        super.initView();
        initCurrentLanguage();
    }

    private void initCurrentLanguage() {
        int languageIndex = ConfigModel.getLanguage(mContext, 0);
        mLanguage = LanguageModel.getLanguage(languageIndex);
        int rid = 0;
        switch (mLanguage) {
            case CHINESE:
                rid = R.id.language_cn;
                break;
            case ENGLISH:
                rid = R.id.language_en;
                break;
        }
        if (rid != 0) {
            mRadioGroup.check(rid);
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.language_cn:
                        mLanguage = LanguageModel.Language.CHINESE;
                        break;
                    case R.id.language_en:
                        mLanguage = LanguageModel.Language.ENGLISH;
                        break;
                }
            }
        });
    }

    @OnClick(R.id.language_switch)
    void switchLanguage() {
        if (LanguageModel.setSystemLanguage(mLanguage)) {
            ConfigModel.setLanguage(mContext, LanguageModel.getLanguageIndex(mLanguage));
        }
    }
}
