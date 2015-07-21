package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.RadioList;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnItemClickEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();

    private ListView listView;
    private CompositeSubscription compositeSubscription;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        compositeSubscription = new CompositeSubscription();
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) root.findViewById(R.id.listView);
        String[] items = getResources().getStringArray(R.array.main_categories_list);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        listView.setAdapter(listAdapter);
        Observable observable = WidgetObservable.itemClicks(listView);
        Observer userDataObserver = new Observer<User>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
                e.printStackTrace();
            }

            @Override
            public void onNext(User user) {
                Log.d(TAG, "onNext");
                //
            }
        };

        compositeSubscription.add(Observable.just(userDataObserver).subscribe());
        // Check User info
        Observable<User> userObservable = new RestService().fetchUserInfoService().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        compositeSubscription.add(userObservable.subscribe(userDataObserver));
        /*compositeSubscription.add(
                observable.
                        map(itemClickEvent -> ((OnItemClickEvent) itemClickEvent).position()).
                        flatMap((Func1<Integer, Observable>) position -> {
                            Log.d(TAG, "-> " + position);
                            switch (position) {
                                case 5: {
                                    // test element
                                    RestService service = new RestService();
                                    return service.fetchUserInfoService().subscribeOn(Schedulers.io());
                                }
                                default: {
                                    return null;
                                }
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(userDataObserver)
        );*/
        Observer<RadioList> radioObserver = new Observer<RadioList>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Radio");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(RadioList radioData) {
                Log.d(TAG, "onNext Radio");
                Log.d(TAG, "radio count -> " + radioData.getData().size());
            }
        };
        Observable radioObservable = new RestService().fetchRadioInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        compositeSubscription.add(radioObservable.subscribe(radioObserver));
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
