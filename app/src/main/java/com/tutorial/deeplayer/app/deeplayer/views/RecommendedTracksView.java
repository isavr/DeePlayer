package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.tutorial.deeplayer.app.deeplayer.adapters.TrackAdapter;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouriteTracksViewModel;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedTrackViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.items.TrackItemView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;

/**
 * Created by ilya.savritsky on 17.08.2015.
 */
public class RecommendedTracksView extends LinearLayout
        implements TrackItemView.OnTrackItemFavouriteStatusInteractionListener {
    public static final String TAG = RecommendedTracksView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private OnTrackItemInteractionListener listener;
    private TrackAdapter adapter;

    @Bind(android.R.id.list)
    AbsListView mListView;

    FavouriteTracksViewModel trackViewModel;

    public RecommendedTracksView(Context context) {
        super(context);
    }

    public RecommendedTracksView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendedTracksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupChildren();
    }


    private void setupChildren() {
        ButterKnife.bind(this);
    }

    public void setViewModel(@Nullable FavouriteTracksViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            trackViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updateTrackList, this::onError);
        }
    }

    private <U> void updateTrackList(U u) {
        Log.d(TAG, "Update track list");
        if (listener != null) {
            listener.onStopProgress();
        }
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapter != null) {
            Cursor c = (Cursor) adapter.getItem(position);
            if (c != null && listener != null) {
                Log.d(TAG, "track clicked");
                Track track = DataContract.TrackConverter.convertFromCursor(c);
                listener.onTrackItemInteraction(track);
            }
        }
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error");
        if (listener != null) {
            listener.onError(throwable);
        }
    }

    public void setListener(@Nullable OnTrackItemInteractionListener listener) {
        this.listener = listener;
    }

    public void clean() {
        adapter.remove();
        adapter = null;
        mListView.setAdapter(null);
    }

    @Override
    public void onTrackItemFavouriteStatusChanged(@NonNull Track track, boolean isFavourite) {
        if (isFavourite) {
            addTrackToFavourite(track);
        } else {
            removeTrackFromFavourite(track);
        }
    }

    public void addTrackToFavourite(@NonNull final Track track) {
        if (trackViewModel != null) {
            rxBinderUtil.bindProperty(trackViewModel.addTrackToFavourite(track),
                    getFavouriteStatusChangeObserver(track, true));
        }
    }

    public void removeTrackFromFavourite(@NonNull final Track track) {
        if (trackViewModel != null) {
            rxBinderUtil.bindProperty(trackViewModel.removeTrackFromFavourite(track),
                    getFavouriteStatusChangeObserver(track, false));
        }
    }

    private Observer<Boolean> getFavouriteStatusChangeObserver(@NonNull final Track track, final boolean toFavourite) {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                trackFavouriteStatusChanged(track, toFavourite, false);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                trackFavouriteStatusChanged(track, toFavourite, aBoolean);
            }
        };
    }

    private void trackFavouriteStatusChanged(@NonNull final Track track, final boolean toFavourite,
                                             final boolean isSuccess) {
        if (isSuccess) {
            track.setFavourite(toFavourite);
            Log.d(TAG, "Artist status updated! Id - " + track.getId() + " Title - " + track.getTitle() +
                    " Status - " + track.isFavourite());
            getContext().getApplicationContext().getContentResolver().update(
                    SchematicDataProvider.Tracks.withId(track.getId()),
                    DataContract.TrackConverter.convertFrom(track)[DataContract.getTrackIndex()], null, null);
            Log.d(TAG, "ALL IS OK");
        } else {
            getContext().getApplicationContext().getContentResolver().notify();
        }
    }

    public void onLoadFinish(Cursor data) {
        if (adapter == null) {
            adapter = new TrackAdapter(getContext(), data, false);
            adapter.setListener(this);
            mListView.setAdapter(adapter);
        } else {
            adapter.changeCursor(data);
        }
    }

    public void onLoaderReset() {
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }

    public interface OnTrackItemInteractionListener {
        void onTrackItemInteraction(@NonNull Track track);

        void onStopProgress();

        void onError(Throwable err);
    }
}
