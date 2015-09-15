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
import android.widget.LinearLayout;

import com.tutorial.deeplayer.app.deeplayer.adapters.ArtistAdapter;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouriteArtistViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.items.ArtistItemView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
public class RecommendedArtistsView extends LinearLayout
        implements ArtistItemView.OnArtistItemFavouriteStatusInteractionListener {
    public static final String TAG = RecommendedAlbumsView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private OnArtistItemInteractionListener listener;
    private ArtistAdapter adapter;

    @Bind(android.R.id.list)
    AbsListView mListView;

    FavouriteArtistViewModel artistViewModel;

    public RecommendedArtistsView(Context context) {
        super(context);
    }

    public RecommendedArtistsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendedArtistsView(Context context, AttributeSet attrs, int defStyleAttr) {
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
            mListView.setNestedScrollingEnabled(true);
        }
    }

    public void setViewModel(@Nullable FavouriteArtistViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            artistViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updateArtistList, this::onError);
        }
    }

    private <U> void updateArtistList(U u) {
        if (listener != null) {
            listener.onStopProgress();
        }
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapter != null) {
            Cursor c = (Cursor) adapter.getItem(position);
            if (c != null && listener != null) {
                Log.d(TAG, "artist clicked");
                Artist artist = DataContract.ArtistConverter.convertFromCursor(c);
                listener.onArtistItemInteraction(artist);
            }
        }
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error! ");
        if (listener == null) {
            Log.d(TAG, "NULL listener!");
        }
        if (listener != null && throwable != null) {
            listener.onError(throwable);
            Snackbar.make(getRootView(), throwable.getMessage(), Snackbar.LENGTH_SHORT)
                    .setAction("Hide", v -> {
                    }).show();
        }
    }

    public void setListener(@Nullable OnArtistItemInteractionListener listener) {
        this.listener = listener;
    }

    public void clean() {
        adapter.remove();
        adapter = null;
        mListView.setAdapter(null);
    }

    @Override
    public void onArtistItemFavouriteStatusChanged(@NonNull Artist artist, boolean isFavourite) {
        if (isFavourite) {
            addArtistToFavourite(artist);
        } else {
            removeArtistFromFavourite(artist);
        }
    }

    public void addArtistToFavourite(@NonNull final Artist artist) {
        if (artistViewModel != null) {
            rxBinderUtil.bindProperty(artistViewModel.addArtistToFavourite(artist),
                    getFavouriteStatusChangeObserver(artist, true));
        }
    }

    public void removeArtistFromFavourite(@NonNull final Artist artist) {
        if (artistViewModel != null) {
            rxBinderUtil.bindProperty(artistViewModel.removeArtistFromFavourite(artist),
                    getFavouriteStatusChangeObserver(artist, false));
        }
    }

    private Observer<Boolean> getFavouriteStatusChangeObserver(@NonNull final Artist artist, final boolean toFavourite) {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                artistFavouriteStatusChanged(artist, toFavourite, false);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                artistFavouriteStatusChanged(artist, toFavourite, aBoolean);
            }
        };
    }

    private void artistFavouriteStatusChanged(@NonNull final Artist artist, final boolean toFavourite,
                                              final boolean isSuccess) {
        if (isSuccess) {
            artist.setFavourite(toFavourite);
            Log.d(TAG, "Artist status updated! Id - " + artist.getId() + " Title - " + artist.getName() +
                    " Status - " + artist.isFavourite());
            getContext().getApplicationContext().getContentResolver().update(SchematicDataProvider.Artists.withId(artist.getId()),
                    DataContract.ArtistConverter.convertFrom(artist), null, null);
            Log.d(TAG, "ALL IS OK");
        } else {
            getContext().getApplicationContext().getContentResolver().notify();
        }
    }

    public void onLoadFinish(Cursor data) {
        if (adapter == null) {
            adapter = new ArtistAdapter(getContext(), data, false);
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

    public interface OnArtistItemInteractionListener {
        void onArtistItemInteraction(@NonNull Artist artist);

        void onStopProgress();

        void onError(Throwable err);
    }
}
