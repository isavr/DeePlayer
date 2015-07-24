package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;

import com.squareup.picasso.Picasso;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by ilya.savritsky on 22.07.2015.
 */
public class RadioView extends RelativeLayout {
    @Bind(R.id.icon_view)
    ImageView iconView;
    @Bind(R.id.title_text)
    TextView titleView;
    @Bind(R.id.favourite_check)
    CheckBox checkFavouriteView;

    private Radio value;

    public RadioView(Context context) {
        super(context, null);
    }

    public RadioView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RadioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChildren();
    }

    public static RadioView inflate(LayoutInflater inflater, ViewGroup parent) {
        RadioView radioView = (RadioView) inflater.inflate(R.layout.radio_view, parent, false);
        radioView.setupChildren();
        return radioView;
    }


    private void setupChildren() {
        ButterKnife.bind(this);
    }

    public void bindToData(Radio radio) {
        setTitle(radio.getTitle());
        setIcon(radio.getPictureMedium());
        setFavourite(radio.isFavourite());
        this.value = radio;
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setIcon(String url) {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            Picasso.with(getContext()).load(url).into(iconView);
        }
    }

    public void setFavourite(boolean isFavourite) {
        checkFavouriteView.setChecked(isFavourite);
    }

    @OnCheckedChanged(R.id.favourite_check)
    public void itemStatusChanged(CompoundButton button, boolean value) {
        if (this.value == null || this.value.isFavourite() != value) {
            // perform change event
            if (value) {

            } else {

            }
        }
    }


    public ImageView getIcon() {
        return iconView;
    }
}
