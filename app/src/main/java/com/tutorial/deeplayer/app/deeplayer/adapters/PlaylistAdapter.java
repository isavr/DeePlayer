package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.views.items.PlaylistItemView;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public class PlaylistAdapter extends CursorAdapter {
    public PlaylistAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public PlaylistAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public PlaylistAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        setup(context);
    }

    private void setup(Context context) {
//        this.layoutInflator = LayoutInflater.from(context);
    }

    public void remove() {
        changeCursor(null);
        notifyDataSetInvalidated();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        PlaylistItemView view = PlaylistItemView.inflate(LayoutInflater.from(context), parent);
//        view.setListener(listener);
        view.bindToData(DataContract.PlaylistConverter.convertFromCursor(cursor));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view instanceof PlaylistItemView) {
            PlaylistItemView playlistItemView = (PlaylistItemView) view;
//            artistItemView.setListener(listener);
            playlistItemView.bindToData(DataContract.PlaylistConverter.convertFromCursor(cursor));
        }
    }
}
