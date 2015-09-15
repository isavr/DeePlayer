package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.activities.MixActivity;
import com.tutorial.deeplayer.app.deeplayer.activities.RecommendationsActivity;
import com.tutorial.deeplayer.app.deeplayer.activities.UserLibraryActivity;
import com.tutorial.deeplayer.app.deeplayer.adapters.MainViewAdapter;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.MainViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
public class MainActivityView extends FrameLayout {
    public static final String TAG = MainActivityView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);

//    @Bind(R.id.listView)
//    ListView listView;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

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
//        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
//        listView.setAdapter(listAdapter);
        MainViewAdapter adapter = new MainViewAdapter(getContext(), items, menuClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

//        deezerConnect = new DeezerConnect(DeePlayerApp.get(),
//                DeePlayerApp.get().getString(R.string.app_id));
//        SessionStore sessionStore = new SessionStore();
//        sessionStore.restore(deezerConnect, DeePlayerApp.get());
    }

    private MainViewAdapter.ViewHolderClicks menuClickListener = new MainViewAdapter.ViewHolderClicks() {
        @Override
        public void onItemClick(String key) {
            if (key == null) {
                return;
            }
            switch (key) {
                case "Library": {
                    Intent intent = new Intent(getContext(), UserLibraryActivity.class);
                    getContext().startActivity(intent);
                    break;
                }
                case "Hear This": {
                    Intent intent = new Intent(getContext(), RecommendationsActivity.class);
                    getContext().startActivity(intent);
                    break;
                }
                case "Mixes": {
                    Intent intent = new Intent(getContext(), MixActivity.class);
                    getContext().startActivity(intent);
                    break;
                }
                default: {

                }
            }
        }
    };

    public void setViewModel(MainViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {

            mainViewModel = viewModel;
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::dataUpdated, this::onError);
            //rxBinderUtil.bindProperty(viewModel.getRepository(), this::loginSuccessfull);
            //rxBinderUtil.bindProperty(viewModel.getSubject(), this::loginSuccessfull);
            //rxBinderUtil.bindProperty(ViewObservable.clicks(loginButton, false), this::attemptLogin);
        }
    }

    private <U> void dataUpdated(U u) {
        Log.d(TAG, "data updated");
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error");
    }

//    @OnItemClick(R.id.listView)
//    public void listViewItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//        switch (position) {
//            case 0: {
//                Intent intent = new Intent(getContext(), UserLibraryActivity.class);
//                getContext().startActivity(intent);
//                break;
//            }
//            case 1: {
//                Intent intent = new Intent(getContext(), RecommendationsActivity.class);
//                getContext().startActivity(intent);
//                break;
//            }
//            case 4: {
//                Intent intent = new Intent(getContext(), MixActivity.class);
//                getContext().startActivity(intent);
//                break;
//            }
//            default: {
//
//            }
//        }
//    }
}
