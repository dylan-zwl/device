package com.tapc.platform.utils;


import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2017/9/8.
 */
public class RxBus {
    private static RxBus sInstance;
    private Subject<Object> mSubject;
    private HashMap<Object, CompositeDisposable> mDisposableHashMap;

    public static RxBus getsInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    private RxBus() {
        mSubject = PublishSubject.create().toSerialized();
        mDisposableHashMap = new HashMap<>();
    }

    /**
     * 发送事件     *     * @param event 递送的事件
     */
    public void post(Object event) {
        mSubject.onNext(event);
    }

    /**
     * 返回指定类型的带背压的Flowable实例     *     * @param type     * @param <T>     * @return
     */
    private <T> Flowable<T> getObservable(Class<T> type) {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER).ofType(type);
    }

    /**
     * 订阅事件     * @param subscriber 订阅者对象     * @param type 事件的类型     * @param next 事件的处理程序     * @param error
     * 事件的异常处理
     * * @param <T>
     */
    public <T> void subscribe(Object subscriber, Class<T> type, Consumer<T> next) {
        Disposable disposable = getObservable(type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(next);
        addSubscription(subscriber, disposable);
    }

    public <T> void subscribe(Object subscriber, Class<T> type, Consumer<T> next, Consumer<Throwable> error) {
        Disposable disposable = getObservable(type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(next, error);
        addSubscription(subscriber, disposable);
    }

    public <T> void subscribe(Object subscriber, Class<T> type, Scheduler scheduler, Consumer<T> next) {
        Disposable disposable = getObservable(type).subscribeOn(Schedulers.io()).observeOn(scheduler)
                .subscribe(next);
        addSubscription(subscriber, disposable);
    }

    public <T> void subscribe(Object subscriber, Class<T> type, Scheduler scheduler, Consumer<T> next,
                              Consumer<Throwable> error) {
        Disposable disposable = getObservable(type).subscribeOn(Schedulers.io()).observeOn(scheduler)
                .subscribe(next, error);
        addSubscription(subscriber, disposable);
    }

    /**
     * 保存订阅后的disposable     *     * @param subscriber     * @param disposable
     */
    private void addSubscription(Object subscriber, Disposable disposable) {
        if (mDisposableHashMap.get(subscriber) != null) {
            mDisposableHashMap.get(subscriber).add(disposable);
        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            mDisposableHashMap.put(subscriber, compositeDisposable);
        }
    }

    /**
     * 是否有观察者订阅     *     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

    /**
     * 取消订阅     *     * @param subscriber
     */
    public void unSubscribe(Object subscriber) {
        if (mDisposableHashMap.containsKey(subscriber)) {
            mDisposableHashMap.get(subscriber).dispose();
            mDisposableHashMap.remove(subscriber);
        }
    }
}
