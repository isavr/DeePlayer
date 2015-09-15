package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.views.items.AlbumItemView;

/**
 * Created by ilya.savritsky on 29.07.2015.
 */
public class AlbumAdapter extends CursorAdapter {
    private static final String TAG = AlbumAdapter.class.getSimpleName();
    private AlbumItemView.OnAlbumItemFavouriteStatusInteractionListener listener;

    public AlbumAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public AlbumAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public AlbumAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        setup(context);
    }

    private void setup(Context context) {
        //this.layoutInflator = LayoutInflater.from(context);
    }


    public void setListener(AlbumItemView.OnAlbumItemFavouriteStatusInteractionListener listener) {
        this.listener = listener;
    }

    public void remove() {
        listener = null;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        AlbumItemView view = AlbumItemView.inflate(LayoutInflater.from(context), parent);
        view.setListener(listener);
        //view.bindToData(DataContract.AlbumConverter.convertFromCursor(cursor));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view instanceof AlbumItemView) {
            AlbumItemView albumItemView = (AlbumItemView) view;
            albumItemView.setListener(listener);
            albumItemView.bindToData(DataContract.AlbumConverter.convertFromCursor(cursor));
        } else {
            Log.d(TAG, "STRANGE ERROR!");
        }
    }
}
