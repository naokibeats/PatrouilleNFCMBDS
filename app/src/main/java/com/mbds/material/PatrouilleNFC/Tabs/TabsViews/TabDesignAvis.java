package com.mbds.material.PatrouilleNFC.Tabs.TabsViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.naokistudio.material.PatrouilleNFC.R;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewAdapters.ScanAdapter;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses.Scan;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewUtils.ItemClickSupport;
import com.mbds.material.PatrouilleNFC.Tabs.TabsUtils.SlidingTabLayout;
import com.mbds.material.PatrouilleNFC.Utils.JsonParser;
import com.mbds.material.PatrouilleNFC.Utils.ScrollManagerToolbarTabs;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class TabDesignAvis extends Fragment {

    String urlPost;
    JSONArray jsonVideoList;
    ArrayList<Scan> scans;
    SwipeRefreshLayout swipeRefreshLayout;
    String[] designTitle, designExcerpt, designImage, designImageFull, designContent;
    Boolean error = false;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    View view;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    TypedValue typedValueToolbarHeight = new TypedValue();
    SlidingTabLayout tabs;
    int recyclerViewPaddingTop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_design, container, false);
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        recyclerViewDesign(view);
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

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        urlPost = "http://naokistudio.zz.mu/video.php";

        new AsyncTaskNewsParseJson().execute(urlPost);

        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                sharedPreferences.edit().putString("TITLE", designTitle[position]).apply();
                sharedPreferences.edit().putString("CONTENT", designContent[position]).apply();
                sharedPreferences.edit().putString("EXCERPT", designExcerpt[position]).apply();
                sharedPreferences.edit().putString("IMAGE", designImageFull[position]).apply();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(designContent[position]));
                startActivity(browserIntent);
            }
        });

    }

    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            try {
                jsonVideoList=  JsonParser.readJsonArrayFromUrl(urlPost);
                int rowNumber = jsonVideoList.length();
                sharedPreferences.edit().putString("DESIGN", jsonVideoList.toString()).apply();
                designTitle = new String[rowNumber];
                designExcerpt = new String[rowNumber];
                designContent = new String[rowNumber];
                designImage = new String[rowNumber];
                designImageFull = new String[rowNumber];
                for (int i = 0; i < rowNumber; i++) {
                    designTitle[i] = Html.fromHtml(jsonVideoList.getJSONObject(i).getString("title")).toString();
                    designExcerpt[i] = Html.fromHtml(jsonVideoList.getJSONObject(i).getString("description")).toString();
                    designContent[i] = Html.fromHtml(jsonVideoList.getJSONObject(i).getString("content")).toString();
                    designImage[i] =  "http://naokistudio.zz.mu/images/health/thumbs/"+ Html.fromHtml(jsonVideoList.getJSONObject(i).getString("thumbs")).toString();
                    designImageFull[i] = "http://naokistudio.zz.mu/images/health/"+ Html.fromHtml(jsonVideoList.getJSONObject(i).getString("image")).toString();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                designTitle = new String[0];
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            scans = new ArrayList<>();
            if (designTitle.length != 0) {
                for (int i = 0 ; i < jsonVideoList.length(); i++) {
                    scans.add(new Scan(designTitle[i], designExcerpt[i], designImage[i]));
                }
            }
            if (error) {
                //Toast.makeText(getActivity(), "Connexion Error", Toast.LENGTH_LONG).show();
                //PnToast.show(getActivity(), "Connexion Error", true);

            }
         //   recyclerViewAdapter = new ScanAdapter(getActivity(), sanction);
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
                new AsyncTaskNewsParseJson().execute(urlPost);
            }
        });
    }
    public int convertToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
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
