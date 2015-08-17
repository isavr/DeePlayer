package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.views.items.TrackItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya.savritsky on 17.08.2015.
 */
public class TrackAdapter extends BaseAdapter {

    private LayoutInflater layoutInflator;
    private List<Track> items;
    private TrackItemView.OnTrackItemFavouriteStatusInteractionListener listener;

    public TrackAdapter(Context context, TrackItemView.OnTrackItemFavouriteStatusInteractionListener listener) {
        this.layoutInflator = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    public void add(Track item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void add(List<Track> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(Track item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void remove() {
        //items.clear();
        layoutInflator = null;
        items = null;
        listener = null;
    }

    public void updateItem(@NonNull Track track) {
        int index = 0;
        for (int i = 0; i < items.size(); ++i) {
            Track currTrack = items.get(i);
            if (track.getId().equals(currTrack.getId())) {
                index = i;
                break;
            }
        }
        items.set(index, track);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Track getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !(convertView instanceof TrackItemView)) {
            convertView = TrackItemView.inflate(layoutInflator, parent);
        }
        Track track = getItem(position);
        if (convertView != null) {
            TrackItemView trackItemView = (TrackItemView) convertView;
            trackItemView.setListener(listener);
            trackItemView.bindToData(track);
            return trackItemView;
        }
        return convertView;
    }
}