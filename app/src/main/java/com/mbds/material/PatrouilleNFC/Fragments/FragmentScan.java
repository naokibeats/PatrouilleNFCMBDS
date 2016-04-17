package com.mbds.material.PatrouilleNFC.Fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mbds.material.PatrouilleNFC.MainActivity;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses.Scan;
import com.naokistudio.material.PatrouilleNFC.R;
import com.mbds.material.PatrouilleNFC.Tabs.TabsAdapters.TabsDesignViewPagerAdapter;
import com.mbds.material.PatrouilleNFC.Tabs.TabsUtils.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentScan extends Fragment {

    String urlPost;
    JSONObject jsonObjectDesignPosts;
    JSONArray jsonArrayDesignContent;
    ArrayList<Scan> scans;
    SwipeRefreshLayout swipeRefreshLayout;
    String[] designTitle, designExcerpt, designImage, designImageFull, designContent;
    int postNumber = 99;
    Boolean error = false;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    View view;
    SharedPreferences sharedPreferences;
    ViewPager pager;
    TabsDesignViewPagerAdapter tabsDesignViewPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence titles[] = {"Scan", "Sanction"};
    int tabNumber = titles.length;
    int tabsPaddingTop;
    TypedValue typedValueToolbarHeight = new TypedValue();
    Toolbar toolbar;
    FrameLayout statusBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_scan, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Patrouille");

        setupTabs();

        return view;
    }

    public void setupTabs() {
        tabsDesignViewPagerAdapter = new TabsDesignViewPagerAdapter(getFragmentManager(), titles, tabNumber);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(tabsDesignViewPagerAdapter);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {

                return getResources().getColor(R.color.md_white_1000);
            }
        });


        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }else{
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 21) {
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }
            if (Build.VERSION.SDK_INT < 19) {
                tabsPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        tabs.setPadding(convertToPx(48), tabsPaddingTop, convertToPx(16), 0);

        tabs.setViewPager(pager);
    }

    public int convertToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

