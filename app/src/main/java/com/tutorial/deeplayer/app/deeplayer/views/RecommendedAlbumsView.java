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

import com.tutorial.deeplayer.app.deeplayer.adapters.AlbumAdapter;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouriteAlbumsViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.items.AlbumItemView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;

/**
 * Created by ilya.savritsky on 28.07.2015.
 */
public class RecommendedAlbumsView extends FrameLayout
        implements AlbumItemView.OnAlbumItemFavouriteStatusInteractionListener {
    public static final String TAG = RecommendedAlbumsView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private OnAlbumItemInteractionListener listener;
    private AlbumAdapter adapter;

    @Bind(android.R.id.list)
    AbsListView mListView;

    FavouriteAlbumsViewModel albumsViewModel;

    public RecommendedAlbumsView(Context context) {
        super(context, null);
    }

    public RecommendedAlbumsView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RecommendedAlbumsView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setViewModel(@Nullable FavouriteAlbumsViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            albumsViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updateAlbumList, this::onError);
        }
    }

    private <U> void updateAlbumList(U u) {
        if (listener != null) {
            listener.onStopProgress();
        }
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapter != null) {
            Cursor c = (Cursor) adapter.getItem(position);
            if (c != null && listener != null) {
                Log.d(TAG, "album clicked");
                Album album = DataContract.AlbumConverter.convertFromCursor(c);
                listener.onAlbumItemInteraction(album);
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

    public void setListener(@Nullable OnAlbumItemInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAlbumItemFavouriteStatusChanged(@NonNull Album album, boolean isFavourite) {
        if (isFavourite) {
            addAlbumToFavourite(album);
        } else {
            removeAlbumFromFavourite(album);
        }
    }

    public void addAlbumToFavourite(@NonNull final Album album) {
        if (albumsViewModel != null) {
            rxBinderUtil.bindProperty(albumsViewModel.addAlbumToFavourite(album),
                    getFavouriteStatusChangeObserver(album, true));
        }
    }

    public void removeAlbumFromFavourite(@NonNull final Album album) {
        if (albumsViewModel != null) {
            rxBinderUtil.bindProperty(albumsViewModel.removeAlbumFromFavourite(album),
                    getFavouriteStatusChangeObserver(album, false));
        }
    }

    private Observer<Boolean> getFavouriteStatusChangeObserver(@NonNull final Album album, final boolean toFavourite) {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                albumFavouriteStatusChanged(album, toFavourite, false);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                albumFavouriteStatusChanged(album, toFavourite, aBoolean);
            }
        };
    }

    private void albumFavouriteStatusChanged(@NonNull final Album album, final boolean toFavourite,
                                             final boolean isSuccess) {
        if (isSuccess) {
            album.setFavourite(toFavourite);
            Log.d(TAG, "Album status updated! Id - " + album.getId() + " Title - " + album.getTitle() +
                    " Status - " + album.isFavourite());
            getContext().getApplicationContext().getContentResolver()
                    .update(SchematicDataProvider.Albums.withId(album.getId()),
                            DataContract.AlbumConverter.convertFrom(album)[DataContract.getAlbumIndex()],
                            null, null);
            Log.d(TAG, "ALL IS OK");
        } else {
            getContext().getApplicationContext().getContentResolver().notify();
        }
    }

    public void clean() {
        adapter.remove();
        adapter = null;
        mListView.setAdapter(null);
    }

    public void onLoadFinish(Cursor data) {
        if (adapter == null) {
            adapter = new AlbumAdapter(getContext(), data, false);
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


    public interface OnAlbumItemInteractionListener {
        void onAlbumItemInteraction(@NonNull Album album);

        void onStopProgress();

        void onError(Throwable err);
    }
}
