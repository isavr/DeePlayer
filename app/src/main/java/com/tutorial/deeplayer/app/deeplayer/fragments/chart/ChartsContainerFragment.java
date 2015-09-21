package com.tutorial.deeplayer.app.deeplayer.fragments.chart;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.adapters.ChartSectionPagerAdapter;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartsContainerFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    public static final String TAG = ChartsContainerFragment.class.getSimpleName();
    private static final String CHART_TAB_KEY = "chart_control_val";
    private static final int CHART_DEF_VALUE = 0;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    ChartSectionPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.pager)
    ViewPager mViewPager;
//    private DeezerConnect deezerConnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected int getPersistedItem(final String keyName, final int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(DeePlayerApp.get().getApplicationContext()).getInt(keyName, defaultValue);
    }

    protected void setPersistedItem(final String keyName, final int position) {
        PreferenceManager.getDefaultSharedPreferences(DeePlayerApp.get().getApplicationContext()).edit().putInt(keyName, position).commit();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setNestedScrollingEnabled(true);
        }
        mSectionsPagerAdapter = new ChartSectionPagerAdapter(getChildFragmentManager(), getContext());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final int tabPosition = getPersistedItem(CHART_TAB_KEY, CHART_DEF_VALUE);
        mViewPager.setCurrentItem(tabPosition);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);
        // deezer
//        deezerConnect = new DeezerConnect(DeePlayerApp.get(), getString(R.string.app_id));
//        SessionStore sessionStore = new SessionStore();
//        sessionStore.restore(deezerConnect, getApplicationContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DeePlayerApp.getRefWatcher().watch(this, "Charts Fragment");
        tabLayout.removeAllTabs();
        tabLayout.setOnTabSelectedListener(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        listener = null;
//        recommendedAlbumsView.clean();
//        recommendedAlbumsView.setListener(null);
        //instrumentation.getLeakTracing().traceLeakage(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (mViewPager != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab selected -> " + position);
            mViewPager.setCurrentItem(position);
            setPersistedItem(CHART_TAB_KEY, position);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab unselected -> " + position);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab reselected -> " + position);
        }
    }
}
