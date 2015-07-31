package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.views.ArtistItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya.savritsky on 31.07.2015.
 */
public class ArtistAdapter extends BaseAdapter {

    private LayoutInflater layoutInflator;
    private List<Artist> items;
    private ArtistItemView.OnArtistItemFavouriteStatusInteractionListener listener;

    public ArtistAdapter(Context context, ArtistItemView.OnArtistItemFavouriteStatusInteractionListener listener) {
        this.layoutInflator = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    public void add(Artist item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void add(List<Artist> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(Artist item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void remove() {
        //items.clear();
        layoutInflator = null;
        items = null;
        listener = null;
    }

    public void updateItem(@NonNull Artist artist) {
        int index = 0;
        for (int i = 0; i < items.size(); ++i) {
            Artist currAtist = items.get(i);
            if (artist.getId().equals(currAtist.getId())) {
                index = i;
                break;
            }
        }
        items.set(index, artist);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Artist getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !(convertView instanceof ArtistItemView)) {
            convertView = ArtistItemView.inflate(layoutInflator, parent);
        }
        Artist album = getItem(position);
        if (convertView != null) {
            ArtistItemView radioView = (ArtistItemView) convertView;

            radioView.setListener(listener);
            radioView.bindToData(album);
            return radioView;
        }

        return convertView;
    }
}
