package com.tapc.platform.model.language;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Administrator on 2017/10/25.
 */

public class LanguageModel {

    public enum Language {
        CHINESE("zh", "CN"),
        ENGLISH("en", "TR");
        String languageCode;
        String countryCode;

        Language(String languageCode, String countryCode) {
        }
    }

    private static Locale getLocale(Language language) {
        return new Locale(language.languageCode, language.countryCode);
    }

    public static boolean setSystemLanguage(Language language) {
        try {
            if (language != null) {
                IActivityManager amn = null;
                Configuration config = null;
                amn = ActivityManagerNative.getDefault();
                config = amn.getConfiguration();
                config.userSetLocale = true;
                config.locale = getLocale(language);
                amn.updateConfiguration(config);
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static int getLanguageIndex(Language language) {
        Language[] list = Language.values();
        for (int i = 0; i < list.length; i++) {
            if (language.equals(list[i])) {
                return i;
            }
        }
        return 0;
    }

    public static Language getLanguage(int index) {
        return Language.values()[index];
    }
}
