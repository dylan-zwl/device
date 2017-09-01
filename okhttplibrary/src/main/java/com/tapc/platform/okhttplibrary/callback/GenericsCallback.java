package com.tapc.platform.okhttplibrary.callback;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * Created by JimGong on 2016/6/23.
 */

public abstract class GenericsCallback<T> extends Callback<T> {
    private IGenericsSerializator mGenericsSerializator;

    public GenericsCallback() {
    }

    public GenericsCallback(IGenericsSerializator serializator) {
        mGenericsSerializator = serializator;
    }

    public void setmGenericsSerializator(IGenericsSerializator genericsSerializator) {
        this.mGenericsSerializator = genericsSerializator;
    }

    @Override

    public T parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        if (entityClass == String.class) {
            return (T) string;
        }
        T bean = mGenericsSerializator.transform(string, entityClass);
        return bean;
    }

}
