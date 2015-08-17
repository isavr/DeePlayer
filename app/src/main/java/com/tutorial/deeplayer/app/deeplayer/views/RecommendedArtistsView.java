package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.tutorial.deeplayer.app.deeplayer.adapters.ArtistAdapter;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedArtistViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.items.ArtistItemView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
public class RecommendedArtistsView extends LinearLayout implements ArtistItemView.OnArtistItemFavouriteStatusInteractionListener {
    public static final String TAG = RecommendedAlbumsView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private OnArtistItemInteractionListener listener;
    private ArtistAdapter adapter;

    @Bind(android.R.id.list)
    AbsListView mListView;

    RecommendedArtistViewModel artistViewModel;

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
        adapter = new ArtistAdapter(getContext(), this);
        mListView.setAdapter(adapter);
    }

    public void setViewModel(@Nullable RecommendedArtistViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            artistViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updateArtistList, this::onError);
        }
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        Artist album = adapter.getItem(position);
        if (listener != null) {
            listener.onArtistItemInteraction(album);
        }
    }

    private void updateArtistList(List<Artist> artists) {
        Log.d(TAG, "update Artist -> " + artists.size());
        adapter.add(artists);
        if (listener != null) {
            listener.onStopProgress();
        }
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error");
        if (listener != null) {
            listener.onError(throwable);
        }
    }

    public void setListener(@Nullable OnArtistItemInteractionListener listener) {
        this.listener = listener;
    }

    public void clean() {
        adapter.remove();
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
            adapter.updateItem(artist);
            Log.d(TAG, "ALL IS OK");
        } else {
            adapter.updateItem(artist);
        }
    }

    public interface OnArtistItemInteractionListener {
        void onArtistItemInteraction(@NonNull Artist artist);

        void onStopProgress();

        void onError(Throwable err);
    }
}
