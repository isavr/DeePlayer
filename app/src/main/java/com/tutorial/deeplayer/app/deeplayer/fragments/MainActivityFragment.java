package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.activities.MixActivity;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import nl.nl2312.rxcupboard.RxDatabase;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();

    @Bind(R.id.listView)
    ListView listView;
    private CompositeSubscription compositeSubscription;
    private RxDatabase rxCupboard = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);
        //final SQLiteDatabase db = CupboardDbHelper.getConnection(getActivity());
        //rxCupboard = RxCupboard.withDefault(db);


        String[] items = getResources().getStringArray(R.array.main_categories_list);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        listView.setAdapter(listAdapter);


        // check radio

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        compositeSubscription = new CompositeSubscription();

    }

    @OnItemClick(R.id.listView)
    public void listViewItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 4: {
                Intent intent = new Intent(getActivity(), MixActivity.class);
                startActivity(intent);
                break;
            }
            case 5: {
                // Check User info
                //compositeSubscription.add(subscribeForUserUpdates());
                compositeSubscription.add(subscribeForRadioUpdates());
                break;
            }
            default: {

            }
        }
    }


    private Subscription subscribeForUserUpdates() {
        Observer userDataObserver = new Observer<User>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
                DialogFactory.closeAlertDialog(getChildFragmentManager());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
                DialogFactory.showSimpleErrorMessage(getActivity(), getChildFragmentManager(), e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(User user) {
                Log.d(TAG, "onNext");
                //rxCupboard.put(user);
            }
        };
        Observable<User> userObservable = new RestService().fetchUserInfoService().subscribeOn(Schedulers.io());

        return userObservable.subscribe(userDataObserver);
    }

//   private Observable createObservable() {
//        Observable<User> userObservable = new RestService().fetchUserInfoService().subscribeOn(Schedulers.io());
//        Observable<Radio> radioObservable = new RestService().fetchRadioInfo().subscribeOn(Schedulers.io())
//                .flatMap(item -> Observable.from(item.getData()));
//        return Observable.zip(userObservable, radioObservable, (item1, item2) -> {return item1 + item2});
//    }

    private Subscription subscribeForRadioUpdates() {
        DialogFactory.showProgressDialog(getActivity(), getChildFragmentManager());
        Observer<Radio> radioObserver = new Observer<Radio>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Radio");
                DialogFactory.closeAlertDialog(getChildFragmentManager());
            }

            @Override
            public void onError(Throwable e) {
                DialogFactory.showSimpleErrorMessage(getActivity(), getChildFragmentManager(), e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Radio radioData) {
                Log.d(TAG, "onNext Radio");
                //rxCupboard.put(radioData);
            }
        };
        //rxCupboard.delete(Radio.class);
        RestService service = new RestService();
        Observable<Radio> radioObservable = service.fetchRadioInfo().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getData())).observeOn(AndroidSchedulers.mainThread());
//        Observable<Radio> userRadios = service.fetchUserRadioInfo().subscribeOn(Schedulers.io())
//                .flatMap(item -> Observable.from(item.getData())).observeOn(AndroidSchedulers.mainThread());
//
//        return Observable.zip(radioObservable, userRadios, (radio, radio2) -> {
//            radio.setFavourite(false);
//            if (radio.getId() == radio2.getId()) {
//                radio.setFavourite(true);
//            }
//            return radio;
//        }).subscribe(radioObserver);
        return radioObservable.subscribe(radioObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeSubscription.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
