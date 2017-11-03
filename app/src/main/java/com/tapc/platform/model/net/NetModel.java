package com.tapc.platform.model.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tapc.platform.okhttplibrary.OkHttpUtils;
import com.tapc.platform.okhttplibrary.callback.GenericsCallback;
import com.tapc.platform.okhttplibrary.callback.IGenericsSerializator;
import com.tapc.platform.utils.RxBus;

/**
 * Created by Administrator on 2017/8/17.
 */

public class NetModel {
    private static NetModel sInstance;
    private OkHttpUtils mOkHttpUtils;
    private String mUrl;

    public void NetModel() {
        mOkHttpUtils = OkHttpUtils.getInstance();
    }

    public static NetModel getInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new NetModel();
                }
            }
        }
        return sInstance;
    }

    public OkHttpUtils getOkhttpUtils() {
        return mOkHttpUtils;
    }

    public NetModel url(@NonNull String url) {
        mUrl = url;
        return this;
    }

    public void post(Object object, GenericsCallback genericsCallback) {
        genericsCallback.setmGenericsSerializator(serializator);
        String contentStr = "";
        if (object != null) {
            contentStr = new Gson().toJson(object);
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
