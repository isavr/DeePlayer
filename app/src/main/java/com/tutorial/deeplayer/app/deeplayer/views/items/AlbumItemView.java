package com.tutorial.deeplayer.app.deeplayer.views.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;

import com.squareup.picasso.Picasso;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by ilya.savritsky on 29.07.2015.
 */
public class AlbumItemView extends RelativeLayout {
    @Bind(R.id.icon_view)
    ImageView iconView;
    @Bind(R.id.title_text)
    TextView titleView;
    @Bind(R.id.artist_text)
    TextView artistView;
    @Bind(R.id.favourite_check)
    CheckBox checkFavouriteView;

    private OnAlbumItemFavouriteStatusInteractionListener listener;

    private Album value;

    public AlbumItemView(Context context) {
        super(context, null);
    }

    public AlbumItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AlbumItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChildren();
    }

    public static AlbumItemView inflate(LayoutInflater inflater, ViewGroup parent) {
        AlbumItemView albumItemView = (AlbumItemView) inflater.inflate(R.layout.album_view, parent, false);
        albumItemView.setupChildren();
        return albumItemView;
    }


    private void setupChildren() {
        ButterKnife.bind(this);
    }

    public void bindToData(Album album) {
        this.value = album;
        setArtist(album.getArtist());
        setTitle(album.getTitle());
        setIcon(album.getCoverMedium());
        setFavourite(album.isFavourite());
    }

    private void setArtist(Artist artist) {
        if (artist != null) {
            artistView.setText(getContext().getString(R.string.artist_name_text, artist.getName()));
        }
    }

    private void setTitle(String title) {
        titleView.setText(title);
    }

    private void setIcon(String url) {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            Picasso.with(getContext()).load(url).fit().centerInside().into(iconView);
        }
    }

    private void setFavourite(boolean isFavourite) {
        checkFavouriteView.setChecked(isFavourite);
    }


    //TODO: setup listener


    @OnCheckedChanged(R.id.favourite_check)
    public void itemStatusChanged(CompoundButton button, boolean isChecked) {
        if (this.value != null && this.value.isFavourite() != isChecked) {
            // perform change event
            if (listener != null) {
                listener.onAlbumItemFavouriteStatusChanged(this.value, isChecked);
            }
        }
    }

    public void setListener(OnAlbumItemFavouriteStatusInteractionListener listener) {
        this.listener = listener;
    }

    public interface OnAlbumItemFavouriteStatusInteractionListener {
        void onAlbumItemFavouriteStatusChanged(@NonNull Album album, boolean isFavourite);
    }
}
