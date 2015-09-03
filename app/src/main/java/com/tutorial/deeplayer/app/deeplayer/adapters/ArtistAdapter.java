package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.views.items.ArtistItemView;

/**
 * Created by ilya.savritsky on 31.07.2015.
 */
public class ArtistAdapter extends CursorAdapter {

    private LayoutInflater layoutInflator;
    private ArtistItemView.OnArtistItemFavouriteStatusInteractionListener listener;

    public ArtistAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public ArtistAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public ArtistAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        setup(context);
    }

    private void setup(Context context) {
        this.layoutInflator = LayoutInflater.from(context);
    }

    public void setListener(ArtistItemView.OnArtistItemFavouriteStatusInteractionListener listener) {
        this.listener = listener;
    }

    public void remove() {
        layoutInflator = null;
        listener = null;
        changeCursor(null);
        notifyDataSetInvalidated();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ArtistItemView view = ArtistItemView.inflate(LayoutInflater.from(context), parent);
        view.setListener(listener);
        view.bindToData(DataContract.ArtistConverter.convertFromCursor(cursor));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view instanceof ArtistItemView) {
            ArtistItemView artistItemView = (ArtistItemView) view;
            artistItemView.setListener(listener);
            artistItemView.bindToData(DataContract.ArtistConverter.convertFromCursor(cursor));
        }
    }
}
