package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.views.RadioItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya.savritsky on 22.07.2015.
 */
public class RadioAdapter extends BaseAdapter {

    private LayoutInflater layoutInflator;
    private List<Radio> items;
    private RadioItemView.OnRadioItemFavouriteStatusInteractionListener listener;

    public RadioAdapter(Context context, RadioItemView.OnRadioItemFavouriteStatusInteractionListener listener) {
        this.layoutInflator = LayoutInflater.from(context);
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    public void add(Radio item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void add(List<Radio> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(Radio item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void remove() {
        //items.clear();
        layoutInflator = null;
        items = null;
        listener = null;
    }

    public void updateItem(@NonNull Radio radio) {
        int index = 0;
        for (int i = 0; i < items.size(); ++i) {
            Radio currRadio = items.get(i);
            if (radio.getId().equals(currRadio.getId())) {
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
    public Radio getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !(convertView instanceof RadioItemView)) {
            convertView = RadioItemView.inflate(layoutInflator, parent);
        }
        Radio radio = getItem(position);
        if (convertView instanceof RadioItemView) {
            RadioItemView radioView = (RadioItemView) convertView;

            radioView.setListener(listener);
            radioView.bindToData(radio);
            return radioView;
        }

        return convertView;
    }
}
