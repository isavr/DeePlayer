package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.views.items.RadioItemView;

/**
 * Created by ilya.savritsky on 22.07.2015.
 */
public class RadioAdapter extends CursorAdapter {

    private LayoutInflater layoutInflator;
    private RadioItemView.OnRadioItemFavouriteStatusInteractionListener listener;

    public RadioAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public RadioAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public RadioAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        setup(context);
    }

    private void setup(Context context) {
        this.layoutInflator = LayoutInflater.from(context);
    }

    public void setListener(RadioItemView.OnRadioItemFavouriteStatusInteractionListener listener) {
        this.listener = listener;
    }

    public void remove() {
        layoutInflator = null;
        listener = null;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        RadioItemView radioItemView = RadioItemView.inflate(LayoutInflater.from(context), parent);
        radioItemView.setListener(listener);
        radioItemView.bindToData(DataContract.RadioConverter.convertFromCursor(cursor));
        return radioItemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view instanceof RadioItemView) {
            RadioItemView radioItemView = (RadioItemView)view;
            radioItemView.setListener(listener);
            radioItemView.bindToData(DataContract.RadioConverter.convertFromCursor(cursor));
        }
    }

//    private Radio getRadioInfoFromCursor(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex(RadioColumns.ID));
//        String title = cursor.getString(cursor.getColumnIndex(RadioColumns.TITLE));
//        String imgUrl = cursor.getString(cursor.getColumnIndex(RadioColumns.PICTURE_MEDIUM));
//        String type = cursor.getString(cursor.getColumnIndex(RadioColumns.TYPE));
//        int favouriteStatus = cursor.getInt(cursor.getColumnIndex(RadioColumns.IS_FAVOURITE));
//        Radio radio = new Radio();
//        radio.setId(Long.valueOf(id));
//        radio.setPictureMedium(imgUrl);
//        radio.setTitle(title);
//        radio.setType(type);
//        radio.setFavourite(favouriteStatus == 1);
//        return radio;
//    }
}
