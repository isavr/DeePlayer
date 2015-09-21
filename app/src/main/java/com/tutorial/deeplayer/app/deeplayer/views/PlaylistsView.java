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

import com.tutorial.deeplayer.app.deeplayer.adapters.PlaylistAdapter;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.pojo.Playlist;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouritePlaylistsViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public class PlaylistsView extends FrameLayout {
    public static final String TAG = RecommendedAlbumsView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private OnPlaylistItemInteractionListener listener;
    private PlaylistAdapter adapter;

    @Bind(android.R.id.list)
    AbsListView mListView;

    FavouritePlaylistsViewModel playlistsViewModel;

    public PlaylistsView(Context context) {
        super(context, null);
    }

    public PlaylistsView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PlaylistsView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setViewModel(@Nullable FavouritePlaylistsViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {
            playlistsViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::updatePlaylist, this::onError);
        }
    }

    private <U> void updatePlaylist(U u) {
        if (listener != null) {
            listener.onStopProgress();
        }
    }

    @OnItemClick(android.R.id.list)
    public void listViewItemClicked(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapter != null) {
            Cursor c = (Cursor) adapter.getItem(position);
            if (c != null && listener != null) {
                Log.d(TAG, "playlist clicked");
                Playlist playlist = DataContract.PlaylistConverter.convertFromCursor(c);
                listener.onPlaylistItemInteraction(playlist);
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

    public void setListener(@Nullable OnPlaylistItemInteractionListener listener) {
        this.listener = listener;
    }

    public void clean() {
        if (adapter != null) {
            adapter.remove();
        }
        adapter = null;
        mListView.setAdapter(null);
    }

    public void onLoadFinish(Cursor data) {
        if (adapter == null) {
            adapter = new PlaylistAdapter(getContext(), data, false);
//            adapter.setListener(this);
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


    public interface OnPlaylistItemInteractionListener {
        void onPlaylistItemInteraction(@NonNull Playlist playlist);

        void onStopProgress();

        void onError(Throwable err);
    }
}
