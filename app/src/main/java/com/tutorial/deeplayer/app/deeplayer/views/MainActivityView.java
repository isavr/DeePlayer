package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.activities.MixActivity;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.MainViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
public class MainActivityView extends FrameLayout {
    public static final String TAG = MainActivityView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);

    @Bind(R.id.listView)
    ListView listView;

    private MainViewModel mainViewModel;

    public MainActivityView(Context context) {
        super(context, null);
    }

    public MainActivityView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MainActivityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupChildren();
    }


    private void setupChildren() {
        ButterKnife.bind(this);
        String[] items = getResources().getStringArray(R.array.main_categories_list);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        listView.setAdapter(listAdapter);
//        deezerConnect = new DeezerConnect(DeePlayerApp.get(),
//                DeePlayerApp.get().getString(R.string.app_id));
//        SessionStore sessionStore = new SessionStore();
//        sessionStore.restore(deezerConnect, DeePlayerApp.get());
    }

    public void setViewModel(MainViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {

            mainViewModel = viewModel;
            //rxBinderUtil.bindProperty(viewModel.getRepository(), this::loginSuccessfull);
            //rxBinderUtil.bindProperty(viewModel.getSubject(), this::loginSuccessfull);
            //rxBinderUtil.bindProperty(ViewObservable.clicks(loginButton, false), this::attemptLogin);
        }
    }

    @OnItemClick(R.id.listView)
    public void listViewItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 4: {
                Intent intent = new Intent(getContext(), MixActivity.class);
                getContext().startActivity(intent);
                break;
            }
            case 5: {
                // Check User info
                //compositeSubscription.add(subscribeForUserUpdates());
                //compositeSubscription.add(subscribeForRadioUpdates());
                break;
            }
            default: {

            }
        }
    }
}
