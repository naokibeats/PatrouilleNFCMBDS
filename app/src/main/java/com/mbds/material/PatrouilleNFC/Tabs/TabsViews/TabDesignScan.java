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
import android.widget.ImageView;
import android.widget.TextView;

import com.naokistudio.material.PatrouilleNFC.R;
import com.skyfishjy.library.RippleBackground;


public class TabDesignScan extends Fragment {

    View view;
    SharedPreferences sharedPreferences;
    TextView messagenfc;
    boolean start = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_scan, container, false);
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        messagenfc = (TextView) view.findViewById(R.id.messagenfc);
        final RippleBackground rippleBackground=(RippleBackground)view.findViewById(R.id.content);
        ImageView imageView=(ImageView)view.findViewById(R.id.centerImage);
        if(start)
            rippleBackground.startRippleAnimation();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start) {
                    rippleBackground.stopRippleAnimation();
                    start = false;
                }
                else {
                    rippleBackground.startRippleAnimation();
                    start = true;
                }

            }
        });
        return view;

    }
    public void setText(String msg){
        messagenfc.setText(msg);
    }

}

