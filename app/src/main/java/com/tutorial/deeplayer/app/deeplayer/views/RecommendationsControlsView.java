package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.tutorial.deeplayer.app.deeplayer.R;

import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
public class RecommendationsControlsView extends LinearLayout {

    public final String TAG = RecommendationsControlsView.class.getSimpleName();

    private OnTypeSelectedListener listener;

    public RecommendationsControlsView(Context context) {
        super(context, null);
    }

    public RecommendationsControlsView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RecommendationsControlsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupChildren();
    }

    private void setupChildren() {
        ButterKnife.bind(this);
    }

    @OnItemSelected(R.id.type_selector)
    public void onTypeSelected(AdapterView<?> adapter, View view, int position, long id) {
        Log.d(TAG, "type selected at " + position + " with id = " + id);
        if (listener != null) {
            listener.onTypeSelected(RecommendationsTypes.fromId(position));
        }
    }

    public void setListener(@Nullable OnTypeSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnTypeSelectedListener {
        void onTypeSelected(RecommendationsTypes type);
    }
}
