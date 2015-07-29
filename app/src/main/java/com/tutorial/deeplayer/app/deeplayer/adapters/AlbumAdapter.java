package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.views.AlbumItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya.savritsky on 29.07.2015.
 */
public class AlbumAdapter extends BaseAdapter {

    private LayoutInflater layoutInflator;
    private List<Album> items;
    private AlbumItemView.OnAlbumItemFavouriteStatusInteractionListener listener;

    public AlbumAdapter(Context context, AlbumItemView.OnAlbumItemFavouriteStatusInteractionListener listener) {
        this.layoutInflator = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    public void add(Album item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void add(List<Album> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(Album item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void remove() {
        //items.clear();
        layoutInflator = null;
        items = null;
        listener = null;
    }

    public void updateItem(@NonNull Album radio) {
        int index = 0;
        for (int i = 0; i < items.size(); ++i) {
            Album currAlbum = items.get(i);
            if (radio.getId().equals(currAlbum.getId())) {
                index = i;
                break;
            }
        }
        items.set(index, radio);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Album getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !(convertView instanceof AlbumItemView)) {
            convertView = AlbumItemView.inflate(layoutInflator, parent);
        }
        Album album = getItem(position);
        if (convertView != null) {
            AlbumItemView radioView = (AlbumItemView) convertView;

            radioView.setListener(listener);
            radioView.bindToData(album);
            return radioView;
        }

        return convertView;
    }
}
