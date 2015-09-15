package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.views.items.TrackItemView;

/**
 * Created by ilya.savritsky on 17.08.2015.
 */
public class TrackAdapter extends CursorAdapter {
//    private LayoutInflater layoutInflator;
    private TrackItemView.OnTrackItemFavouriteStatusInteractionListener listener;

    public TrackAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public TrackAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public TrackAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        setup(context);
    }

    private void setup(Context context) {
//        this.layoutInflator = LayoutInflater.from(context);
    }

    public void setListener(TrackItemView.OnTrackItemFavouriteStatusInteractionListener listener) {
        this.listener = listener;
    }

    public void remove() {
        //items.clear();
//        layoutInflator = null;
        listener = null;
        changeCursor(null);
        notifyDataSetInvalidated();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        TrackItemView view = TrackItemView.inflate(LayoutInflater.from(context), parent);
        view.setListener(listener);
//        if (cursor != null && !cursor.isBeforeFirst() && !cursor.isAfterLast()) {
//            view.bindToData(DataContract.TrackConverter.convertFromCursor(cursor));
//        }
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view instanceof TrackItemView) {
            TrackItemView trackView = (TrackItemView) view;
            trackView.setListener(listener);
            if (cursor != null && cursor.getCount() != 0 && !cursor.isBeforeFirst() && !cursor.isAfterLast()) {
                trackView.bindToData(DataContract.TrackConverter.convertFromCursor(cursor));
            }
        }
    }
}