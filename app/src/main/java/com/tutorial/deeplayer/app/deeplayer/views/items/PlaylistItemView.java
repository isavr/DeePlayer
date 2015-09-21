package com.tutorial.deeplayer.app.deeplayer.views.items;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.pojo.Playlist;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public class PlaylistItemView extends RelativeLayout {
    @Bind(R.id.icon_view)
    ImageView iconView;
    @Bind(R.id.title_text)
    TextView titleView;
    @Bind(R.id.creator_text)
    TextView creatorText;

    private Playlist value;

    public PlaylistItemView(Context context) {
        super(context, null);
    }

    public PlaylistItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PlaylistItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChildren();
    }

    public static PlaylistItemView inflate(LayoutInflater inflater, ViewGroup parent) {
        PlaylistItemView albumItemView = (PlaylistItemView) inflater.inflate(R.layout.playlist_view, parent, false);
        albumItemView.setupChildren();
        return albumItemView;
    }


    private void setupChildren() {
        ButterKnife.bind(this);
    }

    public void bindToData(Playlist playlist) {
        this.value = playlist;
        setCreator(value.getCreator());
        setTitle(value.getTitle());
        setIcon(value.getPictureMedium());
    }

    private void setCreator(User user) {
        if (user != null) {
            creatorText.setText(getContext().getString(R.string.artist_name_text, user.getName()));
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
}
