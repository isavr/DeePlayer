package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.adapters.RadioAdapter;
import com.tutorial.deeplayer.app.deeplayer.data.CupboardDbHelper;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.RadioList;
import com.tutorial.deeplayer.app.deeplayer.pojo.TrackList;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;

import butterknife.*;
import nl.nl2312.rxcupboard.DatabaseChange;
import nl.nl2312.rxcupboard.RxCupboard;
import nl.nl2312.rxcupboard.RxDatabase;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.lifecycle.LifecycleObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RadioFragment extends BaseFragment {
    public static final String TAG = RadioFragment.class.getSimpleName();

    private CompositeSubscription compositeSubscription;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    @Bind(android.R.id.list)
    AbsListView mListView;

    private SQLiteDatabase db;
    private RxDatabase rxCupboard;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private RadioAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static RadioFragment newInstance(String param1, String param2) {
        RadioFragment fragment = new RadioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RadioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        if (getArguments() != null) {
        }
        // TODO: Change Adapter to display your content
        mAdapter = new RadioAdapter(getActivity());
    }

    private Subscription subscribeForRadioTracks(long radioId) {
        DialogFactory.showProgressDialog(getActivity(), getChildFragmentManager());

        Observer<TrackList> radioObserver = new Observer<TrackList>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted RadioTrackList");
                DialogFactory.closeAlertDialog(getChildFragmentManager());
            }

            @Override
            public void onError(Throwable e) {
                DialogFactory.showSimpleErrorMessage(getActivity(), getChildFragmentManager(), e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(TrackList radioData) {
                Log.d(TAG, "onNext RadioTrack List");

                if (radioData.getData() != null) {
                    Log.d(TAG, "onNext Size -> " + radioData.getData().size());
                }
                //rxCupboard.put(radioData);
            }
        };
        //rxCupboard.delete(Radio.class);
        RestService service = new RestService();
        Observable<TrackList> radioObservable = service.fetchRadioTracks(radioId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return radioObservable.subscribe(radioObserver);
    }

    private Subscription subscribeForRadioUpdates(int type) {
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
            public void onNext(Radio radio) {
                Log.d(TAG, "onNext Radio");
                rxCupboard.put(radio);
            }
        };
        mAdapter.clear();
        RestService service = new RestService();
        Observable<RadioList> baseObservable;
        switch (type) {
            case 1: {
                baseObservable = service.fetchRadioInfo();
                break;
            }
            case 2: {
                baseObservable = service.fetchRadioTopInfo();
                break;
            }
            case 3: {
                baseObservable = service.fetchUserRadioInfo();
                break;
            }
            default: {
                return null;
            }
        }
        Observable<Radio> radioObservable = baseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).flatMap(item -> Observable.from(item.getData()));
        return radioObservable.subscribe(radioObserver);
    }

    private Subscription getRadiosWithStatuses() {
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
            public void onNext(Radio radio) {
                Log.d(TAG, "onNext Radio");
                rxCupboard.put(radio);
            }
        };
        mAdapter.clear();
        RestService service = new RestService();
        Observable<Radio> radioObservable = service.fetchRadioInfo().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getData()));
        Observable<Radio> userFavourites = service.fetchUserRadioInfo().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getUserData()));
        return Observable.concat(radioObservable, userFavourites).observeOn(AndroidSchedulers.mainThread())
                .subscribe(radioObserver);
    }

    private Subscription subscribeForRadioUpdates() {
        DialogFactory.showProgressDialog(getActivity(), getChildFragmentManager());
        Observer<RadioList> radioObserver = new Observer<RadioList>() {
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
            public void onNext(RadioList radioData) {
                Log.d(TAG, "onNext Radio");
                mAdapter.clear();
                if (radioData.getData() != null) {
                    mAdapter.add(radioData.getData());
                }

                //rxCupboard.put(radioData);
            }
        };
        //rxCupboard.delete(Radio.class);
        RestService service = new RestService();
        Observable<RadioList> radioObservable = service.fetchRadioInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return radioObservable.subscribe(radioObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        db = CupboardDbHelper.getConnection(getActivity());
        rxCupboard = RxCupboard.withDefault(db);
        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(getRadiosWithStatuses());//subscribeForRadioUpdates(1));

//        //compositeSubscription.add(rxBind(rxCupboard.query(Radio.class).toList()).subscribe(radios -> {
//        //    mAdapter.clear();
//        //    mAdapter.add(radios);
//        //    mListView.setAdapter(mAdapter);
//        //}));
//        compositeSubscription.add(rxCupboard.changes(Radio.class).subscribe(radioDatabaseChange -> {
//            mAdapter.clear();
//        }));

        compositeSubscription.add(rxBind(rxCupboard.query(Radio.class).toList()).subscribe(radios -> {
            mAdapter.clear();
            mAdapter.add(radios);
        }));

        compositeSubscription.add(rxBind(rxCupboard.changes(Radio.class)).doOnNext(radioDatabaseChange -> {
            if (radioDatabaseChange instanceof DatabaseChange.DatabaseUpdate) {
                Log.d(TAG, "Update db item with id " + radioDatabaseChange.entity().getId());
                mAdapter.add(radioDatabaseChange.entity());
            } else if (radioDatabaseChange instanceof DatabaseChange.DatabaseInsert) {
                mAdapter.add(radioDatabaseChange.entity());
            } else if (radioDatabaseChange instanceof DatabaseChange.DatabaseDelete) {
                mAdapter.removeItem(radioDatabaseChange.entity());
            }
        }).subscribe());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        ButterKnife.bind(this, view);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setAdapter(mAdapter);
        return view;
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        Radio radio = mAdapter.getItem(position);
        //mListener.onFragmentInteraction(radio);
        //compositeSubscription.add(subscribeForRadioTracks(radio.getId()));
    }

    @OnItemSelected(android.R.id.list)
    public void listViewItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Radio radio = mAdapter.getItem(position);
        //mListener.onFragmentInteraction(radio);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        mAdapter.clear();
        mAdapter.remove();
        compositeSubscription.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // DeePlayerApp.get().getRefWatcher().watch(compositeSubscription, "Subscriptions");
        // DeePlayerApp.get().getRefWatcher().watch(rxCupboard, "Database");
        // DeePlayerApp.get().getRefWatcher().watch(this);
        ButterKnife.unbind(this);
        super.onDestroy();

    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    private <T> Observable<T> rxBind(Observable<T> source) {
        return LifecycleObservable.bindFragmentLifecycle(lifecycle(), source);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Radio radio);
    }

}
