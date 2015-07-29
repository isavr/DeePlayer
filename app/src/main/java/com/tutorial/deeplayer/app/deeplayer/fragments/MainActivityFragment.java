package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.MainViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.MainActivityView;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();


    private MainActivityView mainActivityView;
    @Inject
    MainViewModel viewModel;
//    @Inject
//    Instrumentation instrumentation;

    //
    //private CompositeSubscription compositeSubscription;
    //private RxDatabase rxCupboard = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeePlayerApp.get().getGraph().inject(this);
        //viewModel = new MainViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityView = (MainActivityView) view.findViewById(R.id.main_view);
        viewModel.subscribeToDataStore();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityView.setViewModel(viewModel);
    }

    @Override
    public void onPause() {
        super.onPause();
        mainActivityView.setViewModel(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.unsubscribeFromDataStore();
        viewModel.dispose();
        viewModel = null;
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.getRefWatcher().watch(this, "Main Fragment");
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //instrumentation.getLeakTracing().traceLeakage(this);
//    }

//    @OnItemClick(R.id.listView)
//    public void listViewItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//        switch (position) {
//            case 4: {
//                Intent intent = new Intent(getActivity(), MixActivity.class);
//                startActivity(intent);
//                break;
//            }
//            case 5: {
//                // Check User info
//                //compositeSubscription.add(subscribeForUserUpdates());
//                //compositeSubscription.add(subscribeForRadioUpdates());
//                break;
//            }
//            default: {
//
//            }
//        }
//    }
//
//
//    private Subscription subscribeForUserUpdates() {
//        Observer userDataObserver = new Observer<User>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG, "onCompleted");
//                DialogFactory.closeAlertDialog(getChildFragmentManager());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "onError");
//                DialogFactory.showSimpleErrorMessage(getActivity(), getChildFragmentManager(), e.getMessage());
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(User user) {
//                Log.d(TAG, "onNext");
//                //rxCupboard.put(user);
//            }
//        };
//        Observable<User> userObservable = new RestService().fetchUserInfoService().subscribeOn(Schedulers.io());
//
//        return userObservable.subscribe(userDataObserver);
//    }

//   private Observable createObservable() {
//        Observable<User> userObservable = new RestService().fetchUserInfoService().subscribeOn(Schedulers.io());
//        Observable<Radio> radioObservable = new RestService().fetchRadioInfo().subscribeOn(Schedulers.io())
//                .flatMap(item -> Observable.from(item.getData()));
//        return Observable.zip(userObservable, radioObservable, (item1, item2) -> {return item1 + item2});
//    }

//    private Subscription subscribeForRadioUpdates() {
//        DialogFactory.showProgressDialog(getActivity(), getChildFragmentManager());
//        Observer<Radio> radioObserver = new Observer<Radio>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG, "onCompleted Radio");
//                DialogFactory.closeAlertDialog(getChildFragmentManager());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                DialogFactory.showSimpleErrorMessage(getActivity(), getChildFragmentManager(), e.getMessage());
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(Radio radioData) {
//                Log.d(TAG, "onNext Radio");
//                //rxCupboard.put(radioData);
//            }
//        };
//        //rxCupboard.delete(Radio.class);
//        RestService service = new RestService();
//        Observable<Radio> radioObservable = service.fetchRadioInfo().subscribeOn(Schedulers.io())
//                .flatMap(item -> Observable.from(item.getData())).observeOn(AndroidSchedulers.mainThread());
////        Observable<Radio> userRadios = service.fetchUserRadioInfo().subscribeOn(Schedulers.io())
////                .flatMap(item -> Observable.from(item.getData())).observeOn(AndroidSchedulers.mainThread());
////
////        return Observable.zip(radioObservable, userRadios, (radio, radio2) -> {
////            radio.setFavourite(false);
////            if (radio.getId() == radio2.getId()) {
////                radio.setFavourite(true);
////            }
////            return radio;
////        }).subscribe(radioObserver);
//        return radioObservable.subscribe(radioObserver);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        //compositeSubscription.clear();
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        ButterKnife.unbind(this);
//    }

}
