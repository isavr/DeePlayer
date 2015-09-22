package com.tutorial.deeplayer.app.deeplayer.fragments.recommended;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseFragment;
import com.tutorial.deeplayer.app.deeplayer.views.FlowView;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link FlowView.OnFlowInteractionListener} interface
 * to handle interaction events.
 */
public class FlowFragment extends BaseFragment {
    public static final String TAG = FlowFragment.class.getSimpleName();

    private FlowView flowView;
//    @Inject
//    RadioViewModel radioViewModel;

    private FlowView.OnFlowInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flow, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flowView = (FlowView) view.findViewById(R.id.flow_view);
//        DialogFactory.showProgressDialog(this.getActivity(),
//                getActivity().getSupportFragmentManager());
//        radioViewModel.subscribeToDataStore();
    }

    @Override
    public void onResume() {
        super.onResume();
//        flowView.setViewModel(radioViewModel);
        flowView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
//        flowView.setViewModel(null);
        flowView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        radioViewModel.unsubscribeFromDataStore();
//        radioViewModel.dispose();
//        radioViewModel = null;
        DeePlayerApp.getRefWatcher().watch(this, "Flow Fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (FlowView.OnFlowInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFlowInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        flowView.clean();
        flowView.setListener(null);
    }
}
