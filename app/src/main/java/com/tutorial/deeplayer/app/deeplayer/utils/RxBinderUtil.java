package com.tutorial.deeplayer.app.deeplayer.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import rx.*;
import rx.android.internal.Preconditions;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ttuo on 02/08/14.
 */
public class RxBinderUtil {
    final static private String TAG = RxBinderUtil.class.getSimpleName();

    @NonNull
    final private String tag;
    final private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public RxBinderUtil(@NonNull Object target) {
        Preconditions.checkNotNull(target, "Target cannot be null.");

        this.tag = target.getClass().getCanonicalName();
    }

    public void clear() {
        compositeSubscription.clear();
    }

    public <U> void bindProperty(@NonNull final Observable<U> observable,
                                 @NonNull final Action1<U> setter,
                                 @NonNull final Action1<Throwable> onErrorAction) {
        compositeSubscription.add(
                subscribeSetter(observable, setter, onErrorAction, tag));
    }

    public <U> void bindProperty(@NonNull final Observable<U> observable,
                                 @NonNull final Observer<U> observer) {
        Preconditions.checkNotNull(observable, "Observable cannot be null.");
        compositeSubscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(observer));
    }

    static private <U> Subscription subscribeSetter(@NonNull final Observable<U> observable,
                                                    @NonNull final Action1<U> setter,
                                                    @NonNull final Action1<Throwable> onErrorAction,
                                                    @NonNull final String tag) {
        Preconditions.checkNotNull(observable, "Observable cannot be null.");

        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SetterSubscriber<>(setter, onErrorAction, tag));
    }

    static private class SetterSubscriber<U> extends Subscriber<U> {
        final static private String TAG = SetterSubscriber.class.getCanonicalName();

        @NonNull
        final private Action1<U> setter;

        @NonNull
        final private Action1<Throwable> onErrorAction;

        @NonNull
        final private String tag;

        public SetterSubscriber(@NonNull final Action1<U> setter, @NonNull final Action1<Throwable> onErrorAction, @NonNull final String tag) {
            Preconditions.checkNotNull(setter, "Setter cannot be null.");
            Preconditions.checkNotNull(tag, "Tag cannot be null.");

            this.setter = setter;
            this.onErrorAction = onErrorAction;
            this.tag = tag;
        }

        @Override
        public void onCompleted() {
            Log.v(TAG, tag + "." + "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, tag + "." + "onError", e);
            onErrorAction.call(e);
        }

        @Override
        public void onNext(U u) {
            setter.call(u);
        }
    }
}
