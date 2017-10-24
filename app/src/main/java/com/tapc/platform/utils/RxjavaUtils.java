package com.tapc.platform.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/31.
 */

public class RxjavaUtils {
    public static Disposable interval(long initialDelay, long period, TimeUnit unit, Consumer consumer,
                                      ObservableTransformer composer) {
        if (composer != null) {
            return Observable.interval(initialDelay, period, unit).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).compose(composer).subscribe(consumer);
        } else {
            return Observable.interval(initialDelay, period, unit).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).subscribe(consumer);
        }
    }

    public static Disposable create(ObservableOnSubscribe<Object> source, Consumer consumer, ObservableTransformer
            composer) {
        if (composer != null) {
            return Observable.create(source).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).compose(composer).subscribe(consumer);
        } else {
            return Observable.create(source).subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).subscribe(consumer);
        }
    }


    public static void dispose(Disposable disposable) {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
