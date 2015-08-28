package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tutorial.deeplayer.app.deeplayer.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ilya.savritsky on 27.08.2015.
 */
public class FlowView extends LinearLayout {
    public static final String TAG = FlowView.class.getSimpleName();

    @Bind(R.id.start_flow_button)
    Button startFlowButton;

    //    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    private OnFlowInteractionListener listener;

    public FlowView(Context context) {
        super(context, null);
    }

    public FlowView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public FlowView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @OnClick(R.id.start_flow_button)
    public void onStartFlow() {
        if (listener != null) {
            // TODO: fix
            listener.onFlowInteraction(700316071L);
        }
    }

//    public void setViewModel(@Nullable RadioViewModel viewModel) {
//        rxBinderUtil.clear();
//        if (viewModel != null) {
//            radioViewModel = viewModel;
//        }
//    }

//    private void onError(Throwable throwable) {
//        Log.d(TAG, "Handle Error");
//    }

    public void setListener(@Nullable OnFlowInteractionListener listener) {
        this.listener = listener;
    }

    public void clean() {

    }

    public interface OnFlowInteractionListener {
        void onFlowInteraction(long userId);
//        void onStopProgress();
    }
}
