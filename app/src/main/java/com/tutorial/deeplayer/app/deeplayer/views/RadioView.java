package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.tutorial.deeplayer.app.deeplayer.adapters.RadioAdapter;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RadioViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.items.RadioItemView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
public class RadioView extends FrameLayout implements RadioItemView.OnRadioItemFavouriteStatusInteractionListener {
    public static final String TAG = RadioView.class.getSimpleName();

    @Bind(android.R.id.list)
    AbsListView mListView;

    private RadioAdapter radioAdapter;

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private RadioViewModel radioViewModel;
    private OnRadioItemInteractionListener listener;

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
        radioAdapter = new RadioAdapter(getContext(), this);
        mListView.setAdapter(radioAdapter);
    }

    public void setViewModel(@Nullable RadioViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            radioViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updateRadioList, this::onError);
        }
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error");
    }

    public void addRadioToFavourite(@NonNull final Radio radio) {
        if (radioViewModel != null) {
            rxBinderUtil.bindProperty(radioViewModel.addRadioToFavourite(radio), getFavouriteStatusChangeObserver(radio, true));
        }
    }

    public void removeRadioFromFavourite(@NonNull final Radio radio) {
        if (radioViewModel != null) {
            rxBinderUtil.bindProperty(radioViewModel.removeRadioFromFavourite(radio), getFavouriteStatusChangeObserver(radio, false));
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
            radioAdapter.updateItem(radio);
            Log.d(TAG, "ALL IS OK");
        } else {
            radioAdapter.updateItem(radio);
        }
    }

    public void setListener(@Nullable OnRadioItemInteractionListener listener) {
        this.listener = listener;
    }

    private void updateRadioList(List<Radio> radios) {
        Log.d(TAG, "update Radio -> " + radios.size());
        if (listener != null) {
            listener.onStopProgress();
        }
        radioAdapter.add(radios);
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        Radio radio = radioAdapter.getItem(position);
        if (listener != null) {
            listener.onRadioItemInteraction(radio);
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
        radioAdapter.remove();
    }

    public interface OnRadioItemInteractionListener {
        void onRadioItemInteraction(@NonNull Radio radio);

        void onStopProgress();
    }
}
