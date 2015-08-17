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
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by ilya.savritsky on 17.08.2015.
 */
public class TrackItemView extends RelativeLayout {
    @Bind(R.id.icon_view)
    ImageView iconView;
    @Bind(R.id.title_text)
    TextView titleView;
    @Bind(R.id.album_text)
    TextView albumView;
    @Bind(R.id.artist_text)
    TextView artistView;
    @Bind(R.id.favourite_check)
    CheckBox checkFavouriteView;

    public TrackItemView(Context context) {
        super(context);
    }

    public TrackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrackItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChildren();
    }

    private OnTrackItemFavouriteStatusInteractionListener listener;

    private Track value;

    public static TrackItemView inflate(LayoutInflater inflater, ViewGroup parent) {
        TrackItemView trackItemView = (TrackItemView) inflater.inflate(R.layout.track_view, parent, false);
        trackItemView.setupChildren();
        return trackItemView;
    }


    private void setupChildren() {
        ButterKnife.bind(this);
    }

    public void bindToData(Track track) {
        this.value = track;
        setTitle(track.getTitle());
        setArtist(track);
        if (track.getAlbum() != null) {
            setAlbum(track.getAlbum());
            setIcon(track.getAlbum().getCoverMedium());
        }
        setFavourite(track.isFavourite());
    }

    private void setAlbum(Album album) {
        if (album != null) {
            albumView.setText(album.getTitle());
        }
    }

    private void setArtist(Track track) {
        if (track != null && track.getArtist() != null) {
            artistView.setText(getContext().getString(R.string.artist_name_text, track.getArtist().getName()));
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

    @OnCheckedChanged(R.id.favourite_check)
    public void itemStatusChanged(CompoundButton button, boolean isChecked) {
        if (this.value != null && this.value.isFavourite() != isChecked) {
            // perform change event
            if (listener != null) {
                listener.onTrackItemFavouriteStatusChanged(this.value, isChecked);
            }
        }
    }

    public void setListener(OnTrackItemFavouriteStatusInteractionListener listener) {
        this.listener = listener;
    }

    public interface OnTrackItemFavouriteStatusInteractionListener {
        void onTrackItemFavouriteStatusChanged(@NonNull Track track, boolean isFavourite);
    }
}
