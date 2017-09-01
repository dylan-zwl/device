package com.tapc.platform.model.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tapc.platform.okhttplibrary.OkHttpUtils;
import com.tapc.platform.okhttplibrary.callback.GenericsCallback;
import com.tapc.platform.okhttplibrary.callback.IGenericsSerializator;

/**
 * Created by Administrator on 2017/8/17.
 */

public class NetModel {
    private OkHttpUtils mOkHttpUtils;
    private String mUrl;

    public NetModel() {
        mOkHttpUtils = OkHttpUtils.getInstance();
    }

    public OkHttpUtils getOkhttpUtils() {
        return mOkHttpUtils;
    }

    public NetModel url(@NonNull String url) {
        this.mUrl = url;
        return this;
    }

    public void post(Object content, GenericsCallback genericsCallback) {
        genericsCallback.setmGenericsSerializator(serializator);
        String contentStr = "";
        if (content != null) {
            contentStr = new Gson().toJson(content);
        }
        mOkHttpUtils.postString().url(mUrl).tag(this).content(contentStr).build().execute(genericsCallback);
    }

    private IGenericsSerializator serializator = new IGenericsSerializator() {
        @Override
        public <T> T transform(String response, Class<T> classOfT) {
            return new Gson().fromJson(response, classOfT);
        }
    };

    public void stop() {
        mOkHttpUtils.cancelTag(this);
    }
}
