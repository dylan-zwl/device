package com.tapc.platform.model.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tapc.platform.okhttplibrary.OkHttpUtils;
import com.tapc.platform.okhttplibrary.callback.GenericsCallback;
import com.tapc.platform.okhttplibrary.callback.IGenericsSerializator;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/17.
 */

public class NetModel<P> {
    private String mUrl;
    private Map<String, String> mHeaders;

    public void NetModel() {

    }

    public void NetModel(@NonNull String url) {
        mUrl = url;
    }

    public NetModel url(@NonNull String url) {
        mUrl = url;
        return this;
    }

    public NetModel headers(Map<String, String> headers) {
        mHeaders = headers;
        return this;
    }

    public void post(P object, GenericsCallback genericsCallback) {
        genericsCallback.setmGenericsSerializator(sSerializator);
        String contentStr = "";
        if (object != null) {
            contentStr = new Gson().toJson(object);
        }
        OkHttpUtils.getInstance().postString().url(mUrl).headers(mHeaders).tag(this).content(contentStr).build().execute
                (genericsCallback);
    }

    private static IGenericsSerializator sSerializator = new IGenericsSerializator() {
        @Override
        public <T> T transform(String response, Class<T> classOfT) {
            return new Gson().fromJson(response, classOfT);
        }
    };

    public OkHttpUtils getOkhttpUtils() {
        return OkHttpUtils.getInstance();
    }

    public void cancelTag() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
