package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya.savritsky on 22.07.2015.
 */
public class RadioAdapter extends BaseAdapter {

    private LayoutInflater layoutInflator;
    private List<Radio> items;

    public RadioAdapter(Context context) {
        this.layoutInflator = LayoutInflater.from(context);
        this.items = new ArrayList<>();
    }

    public void add(Radio item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void update(Radio item) {
        int ind = this.items.indexOf(item);
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
        if (convertView == null || !(convertView instanceof RadioView)) {
            convertView = RadioView.inflate(layoutInflator, parent);
        }
        Radio radio = getItem(position);
        if (convertView instanceof RadioView) {
            ((RadioView) convertView).bindToData(radio);
        }
        return convertView;
    }
}
