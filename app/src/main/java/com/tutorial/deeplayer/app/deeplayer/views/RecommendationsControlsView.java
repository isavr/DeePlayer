package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tutorial.deeplayer.app.deeplayer.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
@Deprecated
public class RecommendationsControlsView extends LinearLayout {
    private static final String SPINNER_KEY = "recommendations_control_val";
    private static final int SPINNER_DEF_VALUE = 0;

    public final String TAG = RecommendationsControlsView.class.getSimpleName();

    private OnTypeSelectedListener listener;

    @Bind(R.id.type_selector)
    Spinner spinner;

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
        int spinnerVal = getPersistedItem(SPINNER_KEY);
        spinner.setSelection(spinnerVal);

    }

    @OnItemSelected(R.id.type_selector)
    public void onTypeSelected(AdapterView<?> adapter, View view, int position, long id) {
        Log.d(TAG, "type selected at " + position + " with id = " + id);
        if (listener != null) {
            setPersistedItem(position, SPINNER_KEY);
            listener.onTypeSelected(RecommendationsTypes.fromId(position));
        }
    }

    public void setListener(@Nullable OnTypeSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnTypeSelectedListener {
        void onTypeSelected(RecommendationsTypes type);
    }

    private int getPersistedItem(String keyName) {
        return PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()).getInt(keyName, SPINNER_DEF_VALUE);
    }

    protected void setPersistedItem(int position, String keyName) {
        PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()).edit().putInt(keyName, position).commit();
    }
}
