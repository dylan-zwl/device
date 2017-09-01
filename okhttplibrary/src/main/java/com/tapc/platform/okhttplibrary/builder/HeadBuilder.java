package com.tapc.platform.okhttplibrary.builder;


import com.tapc.platform.okhttplibrary.OkHttpUtils;
import com.tapc.platform.okhttplibrary.request.OtherRequest;
import com.tapc.platform.okhttplibrary.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers, id).build();
    }
}
