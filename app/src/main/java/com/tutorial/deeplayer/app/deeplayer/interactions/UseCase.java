package com.tutorial.deeplayer.app.deeplayer.interactions;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a {@link rx.Subscriber}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class UseCase {

    protected abstract Observable buildUseCaseObservable();

    public Subscription build(final Scheduler subscribeOnScheduler,
                              final Scheduler observeOnScheduler,
                              final Observer observer) {
        return this.buildUseCaseObservable()
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(observer);
    }
}
