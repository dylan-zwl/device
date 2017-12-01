package com.tapc.platform.model.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tapc.platform.okhttplibrary.OkHttpUtils;
import com.tapc.platform.okhttplibrary.callback.StringCallback;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/17.
 */

public class NetModel {
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

    public void post(Object object, StringCallback callback) {
        String contentStr = "";
        if (object != null) {
            contentStr = new Gson().toJson(object);
        }
        OkHttpUtils.getInstance().postString().tag(this).url(mUrl).headers(mHeaders).content(contentStr).build().execute
                (callback);
    }

    public void post(Map<String, String> params, StringCallback callback) {
        OkHttpUtils.getInstance().post().tag(this).url(mUrl).headers(mHeaders).params(params).build().execute
                (callback);
    }

    public OkHttpUtils getOkhttpUtils() {
        return OkHttpUtils.getInstance();
    }

    public void cancelTag() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
