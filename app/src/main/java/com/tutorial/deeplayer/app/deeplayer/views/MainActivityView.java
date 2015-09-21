package com.tutorial.deeplayer.app.deeplayer.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.tutorial.deeplayer.app.deeplayer.R;
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

    private OnMainItemInteractionListener listener;

    private MainViewModel mainViewModel;

    private MainViewAdapter adapter;

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
        adapter = new MainViewAdapter(getContext(), items, menuClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

//        deezerConnect = new DeezerConnect(DeePlayerApp.get(),
//                DeePlayerApp.get().getString(R.string.app_id));
//        SessionStore sessionStore = new SessionStore();
//        sessionStore.restore(deezerConnect, DeePlayerApp.get());
    }

    private MainViewAdapter.ViewHolderClicks menuClickListener = key -> {
        if (key == null || listener == null || adapter == null) {
            return;
        }
        listener.selectItemWithKey(key);
        String[] items = getResources().getStringArray(R.array.main_categories_list);
        for (int i = 0; i < items.length; ++i) {
            if (key.equals(items[i])) {
                adapter.selectItemAtPos(i);
                break;
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

    public void clean() {
        listener = null;
        menuClickListener = null;
        if (recyclerView != null) {
            recyclerView.setOnClickListener(null);
            recyclerView.removeAllViews();
        }
    }

    private <U> void dataUpdated(U u) {
        Log.d(TAG, "data updated");
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error");
    }

    public void setListener(OnMainItemInteractionListener listener) {
        this.listener = listener;
    }

    public interface OnMainItemInteractionListener {
        void selectItemWithKey(String key);

        void onError(Throwable err);
    }
}
