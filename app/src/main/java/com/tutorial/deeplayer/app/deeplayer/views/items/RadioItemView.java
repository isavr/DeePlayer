package com.tutorial.deeplayer.app.deeplayer.views.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by ilya.savritsky on 22.07.2015.
 */
public class RadioItemView extends RelativeLayout {
    @Bind(R.id.icon_view)
    ImageView iconView;
    @Bind(R.id.title_text)
    TextView titleView;
    @Bind(R.id.favourite_check)
    CheckBox checkFavouriteView;

    private OnRadioItemFavouriteStatusInteractionListener listener;

    private Radio value;

    public RadioItemView(Context context) {
        super(context, null);
    }

    public RadioItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RadioItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupChildren();
    }

    public static RadioItemView inflate(LayoutInflater inflater, ViewGroup parent) {
        RadioItemView radioItemView = (RadioItemView) inflater.inflate(R.layout.radio_view, parent, false);
        radioItemView.setupChildren();
        return radioItemView;
    }


    private void setupChildren() {
        ButterKnife.bind(this);
    }

    public void bindToData(Radio radio) {
        this.value = radio;
        setTitle(radio.getTitle());
        setIcon(radio.getPictureMedium());
        setFavourite(radio.isFavourite());
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setIcon(String url) {
        if (url != null && Patterns.WEB_URL.matcher(url).matches()) {
            Picasso.with(getContext()).load(url).fit().centerInside().into(iconView);
        }
    }

    public void setFavourite(boolean isFavourite) {
        checkFavouriteView.setChecked(isFavourite);
    }

    @OnCheckedChanged(R.id.favourite_check)
    public void itemStatusChanged(CompoundButton button, boolean isChecked) {
        if (this.value != null && this.value.isFavourite() != isChecked) {
            // perform change event
            if (listener != null) {
                listener.onRadioItemFavouriteStatusChanged(this.value, isChecked);
            }
        }
    }

    public void setListener(OnRadioItemFavouriteStatusInteractionListener listener) {
        this.listener = listener;
    }

    public interface OnRadioItemFavouriteStatusInteractionListener {
        void onRadioItemFavouriteStatusChanged(@NonNull Radio radio, boolean isFavourite);
    }
}
