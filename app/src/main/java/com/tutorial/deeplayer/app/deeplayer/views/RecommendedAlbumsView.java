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

import com.tutorial.deeplayer.app.deeplayer.adapters.AlbumAdapter;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedAlbumsViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observer;

/**
 * Created by ilya.savritsky on 28.07.2015.
 */
public class RecommendedAlbumsView extends FrameLayout implements AlbumItemView.OnAlbumItemFavouriteStatusInteractionListener {
    public static final String TAG = RecommendedAlbumsView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private OnAlbumItemInteractionListener listener;
    private AlbumAdapter adapter;

    @Bind(android.R.id.list)
    AbsListView mListView;

    RecommendedAlbumsViewModel albumsViewModel;

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
        adapter = new AlbumAdapter(getContext(), this);
        mListView.setAdapter(adapter);
    }

    public void setViewModel(@Nullable RecommendedAlbumsViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            albumsViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updateAlbumList);
        }
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        Album album = adapter.getItem(position);
        if (listener != null) {
            listener.onAlbumItemInteraction(album);
        }
    }

    private void updateAlbumList(List<Album> albums) {
        Log.d(TAG, "update Albums -> " + albums.size());
        adapter.add(albums);
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
                radioFavouriteStatusChanged(album, toFavourite, false);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                radioFavouriteStatusChanged(album, toFavourite, aBoolean);
            }
        };
    }

    private void radioFavouriteStatusChanged(@NonNull final Album album, final boolean toFavourite,
                                             final boolean isSuccess) {
        if (isSuccess) {
            album.setFavourite(toFavourite);
            adapter.updateItem(album);
            Log.d(TAG, "ALL IS OK");
        } else {
            adapter.updateItem(album);
        }
    }

    public void clean() {
        adapter.remove();
    }


    public interface OnAlbumItemInteractionListener {
        void onAlbumItemInteraction(@NonNull Album album);
    }
}
