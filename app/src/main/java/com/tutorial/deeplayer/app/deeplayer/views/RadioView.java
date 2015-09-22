package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.tutorial.deeplayer.app.deeplayer.adapters.RadioAdapter;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.utils.FilterableContent;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouriteRadiosViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.items.RadioItemView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
public class RadioView extends FrameLayout
        implements RadioItemView.OnRadioItemFavouriteStatusInteractionListener {
    public static final String TAG = RadioView.class.getSimpleName();
    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    @Bind(android.R.id.list)
    AbsListView mListView;
    private RadioAdapter radioAdapter;
    private FavouriteRadiosViewModel radioViewModel;
    private OnRadioItemInteractionListener listener;
    private FilterableContent filterListener;

    public RadioView(Context context) {
        super(context, null);
    }

    public RadioView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RadioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupChildren();
    }


    private void setupChildren() {
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
            mListView.setNestedScrollingEnabled(true);
        }
    }

    public void setViewModel(@Nullable FavouriteRadiosViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            radioViewModel = viewModel;
            Log.d(TAG, "set view model");
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updateRadioList, this::onError);
            if (viewModel.getFilterObservable() != null) {
                rxBinderUtil.bindProperty(viewModel.getFilterObservable(), this::updateFilter, this::onError);
            }
        }
    }

    private void updateFilter(String s) {
        Log.d(TAG, "Filter update received with data -> " + s);
        if (filterListener != null) {
            // filter update
            filterListener.OnFilterUpdate(s);
        }
    }

    private <U> void updateRadioList(U u) {
        Log.d(TAG, "update Radio List");
        if (listener != null) {
            listener.onStopProgress();
        }
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error");
        if (listener != null && throwable != null) {
            listener.onError(throwable);
            Snackbar.make(getRootView(), throwable.getMessage(), Snackbar.LENGTH_SHORT)
                    .setAction("Hide", v -> {
                    }).show();
        }
    }

    public void addRadioToFavourite(@NonNull final Radio radio) {
        if (radioViewModel != null) {
            rxBinderUtil.bindProperty(radioViewModel.addRadioToFavourite(radio),
                    getFavouriteStatusChangeObserver(radio, true));
        }
    }

    public void removeRadioFromFavourite(@NonNull final Radio radio) {
        if (radioViewModel != null) {
            rxBinderUtil.bindProperty(radioViewModel.removeRadioFromFavourite(radio),
                    getFavouriteStatusChangeObserver(radio, false));
        }
    }

    private Observer<Boolean> getFavouriteStatusChangeObserver(@NonNull final Radio radio, final boolean toFavourite) {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                radioFavouriteStatusChanged(radio, toFavourite, false);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                radioFavouriteStatusChanged(radio, toFavourite, aBoolean);
            }
        };
    }

    private void radioFavouriteStatusChanged(@NonNull final Radio radio, final boolean toFavourite,
                                             final boolean isSuccess) {
        if (isSuccess) {
            radio.setFavourite(toFavourite);
            Log.d(TAG, "Radio status updated! Id - " + radio.getId() + " Title - "
                    + radio.getTitle() + " Status - " + radio.isFavourite());
            getContext().getApplicationContext().getContentResolver().update(SchematicDataProvider.Radios.withId(radio.getId()),
                    DataContract.RadioConverter.convertFrom(radio), null, null);
//            radioAdapter.updateItem(radio);
            Log.d(TAG, "ALL IS OK");
        } else {
            getContext().getApplicationContext().getContentResolver().notify();
//            radioAdapter.updateItem(radio);
        }
    }

    public void setListener(@Nullable OnRadioItemInteractionListener listener) {
        this.listener = listener;
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
        if (radioAdapter != null) {
            Cursor c = (Cursor) radioAdapter.getItem(position);
            if (c != null && listener != null) {
                Log.d(TAG, "radio clicked");
                Radio radio = DataContract.RadioConverter.convertFromCursor(c);
                listener.onRadioItemInteraction(radio);
            }
        }
    }

    @Override
    public void onRadioItemFavouriteStatusChanged(@NonNull Radio radio, boolean isFavourite) {
        if (isFavourite) {
            addRadioToFavourite(radio);
        } else {
            removeRadioFromFavourite(radio);
        }

    }

    public void clean() {
        if (radioAdapter != null) {
            radioAdapter.remove();
        }
    }

    public void onLoadFinish(Cursor data) {
        if (radioAdapter == null) {
            radioAdapter = new RadioAdapter(getContext(), data);
            radioAdapter.setListener(this);
            mListView.setAdapter(radioAdapter);
        } else {
            radioAdapter.changeCursor(data);
        }
    }

    public void onLoaderReset() {
        if (radioAdapter != null) {
            radioAdapter.changeCursor(null);
        }
    }

    public void setFilterListener(FilterableContent filterListener) {
        this.filterListener = filterListener;
    }

    public interface OnRadioItemInteractionListener {
        void onRadioItemInteraction(@NonNull Radio radio);

        void onStopProgress();

        void onError(Throwable err);
    }
}
