package com.mbds.material.PatrouilleNFC.Tabs.TabsViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mbds.material.PatrouilleNFC.DetailActivity;
import com.mbds.material.PatrouilleNFC.Model.Infraction;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewAdapters.ScanAdapter;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses.Scan;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewUtils.ItemClickSupport;
import com.mbds.material.PatrouilleNFC.Tabs.TabsUtils.SlidingTabLayout;
import com.mbds.material.PatrouilleNFC.Utils.JsonParser;
import com.mbds.material.PatrouilleNFC.Utils.PnToast;
import com.mbds.material.PatrouilleNFC.Utils.ScrollManagerToolbarTabs;
import com.mbds.material.PatrouilleNFC.Utils.SessionManager;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabDesignSanction extends Fragment {

    List<Infraction> sanction;
    SwipeRefreshLayout swipeRefreshLayout;
    Boolean error = false;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    View view;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    SlidingTabLayout tabs;
    int recyclerViewPaddingTop;
    String iduser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_design, container, false);
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        recyclerViewDesign(view);
        swipeToRefresh(view);
        return view;
    }

    private void recyclerViewDesign(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDesign);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);

        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SessionManager session = new SessionManager(view.getContext());

        HashMap<String, String> user = session.getUserDetails();
         iduser = user.get(SessionManager.KEY_USER);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        new AsyncTaskNewsParseJson().execute(iduser);

        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerView);


    }

    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, List<Infraction>> {


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected List<Infraction> doInBackground(String... idutilisteur) {

            String id = idutilisteur[0];
            try {

                return Utilitaire.getInfractionByUtilitsateur(id);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                error = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Infraction> result) {

            sanction = result;
            if (error) {
                PnToast.show(getActivity(), "Erreur de connexion", true);
            }
            // Create the recyclerViewAdapter
            recyclerViewAdapter = new ScanAdapter(getActivity(), sanction);
            recyclerView.setAdapter(recyclerViewAdapter);

            swipeRefreshLayout = (android.support.v4.widget.SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeRefreshLayout.setRefreshing(false);

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }
    private void swipeToRefresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        int start = recyclerViewPaddingTop - convertToPx(48), end = recyclerViewPaddingTop + convertToPx(16);
        swipeRefreshLayout.setProgressViewOffset(true, start, end);
        TypedValue typedValueColorPrimary = new TypedValue();
        TypedValue typedValueColorAccent = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValueColorPrimary, true);
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, typedValueColorAccent, true);
        final int colorPrimary = typedValueColorPrimary.data, colorAccent = typedValueColorAccent.data;
        swipeRefreshLayout.setColorSchemeColors(colorPrimary, colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskNewsParseJson().execute(iduser);
            }
        });
    }
    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    public void toolbarHideShow() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                ScrollManagerToolbarTabs manager = new ScrollManagerToolbarTabs(getActivity());
                manager.attach(recyclerView);
                manager.addView(toolbar, ScrollManagerToolbarTabs.Direction.UP);
                manager.setInitialOffset(toolbar.getHeight());
            }
        });
    }
}

