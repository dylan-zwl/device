package com.tapc.platform.utils;


import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
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
    private static RxBus instance;

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private Subject<Object> subject;
    private HashMap<Object, CompositeDisposable> disposableHashMap;

    private RxBus() {
        subject = PublishSubject.create().toSerialized();
        disposableHashMap = new HashMap<>();
    }

    /**
     * 发送事件     *     * @param event 递送的事件
     */
    public void post(Object event) {
        subject.onNext(event);
    }

    /**
     * 返回指定类型的带背压的Flowable实例     *     * @param type     * @param <T>     * @return
     */
    private <T> Flowable<T> getObservable(Class<T> type) {
        return subject.toFlowable(BackpressureStrategy.BUFFER).ofType(type);
    }

    /**
     * 订阅事件     * @param subscriber 订阅者对象     * @param type 事件的类型     * @param next 事件的处理程序     * @param error
     * 事件的异常处理
     * * @param <T>
     */
    public <T> void subscribe(Object subscriber, Class<T> type, Consumer<T> next, Consumer<Throwable> error) {
        Disposable disposable = getObservable(type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe(next, error);
        addSubscription(subscriber, disposable);
    }

    /**
     * 保存订阅后的disposable     *     * @param subscriber     * @param disposable
     */
    private void addSubscription(Object subscriber, Disposable disposable) {
        if (disposableHashMap.get(subscriber) != null) {
            disposableHashMap.get(subscriber).add(disposable);
        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            disposableHashMap.put(subscriber, compositeDisposable);
        }
    }

    /**
     * 是否有观察者订阅     *     * @return
     */
    public boolean hasObservers() {
        return subject.hasObservers();
    }

    /**
     * 取消订阅     *     * @param subscriber
     */
    public void unSubscribe(Object subscriber) {
        if (disposableHashMap.containsKey(subscriber)) {
            disposableHashMap.get(subscriber).dispose();
            disposableHashMap.remove(subscriber);
        }
    }
}
