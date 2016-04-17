package com.mbds.material.PatrouilleNFC.Tabs.TabsAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mbds.material.PatrouilleNFC.Tabs.TabsViews.TabDesignSanction;
import com.mbds.material.PatrouilleNFC.Tabs.TabsViews.TabDesignProfil;
import com.mbds.material.PatrouilleNFC.Tabs.TabsViews.TabDesignScan;
import com.mbds.material.PatrouilleNFC.Tabs.TabsViews.TabDesignAvis;

public class TabsDesignViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];
    int tabNumber;

    public TabsDesignViewPagerAdapter (FragmentManager fragmentManager, CharSequence titles[], int tabNumber) {
        super(fragmentManager);

        this.titles = titles;
        this.tabNumber = tabNumber;

    }


    @Override
    public Fragment getItem(int position) {
        TabDesignScan tabDesignScan = new TabDesignScan();
        TabDesignSanction tabDesignSanction = new TabDesignSanction();
        switch (position) {
            case 0:
                return tabDesignScan;
            case 1:
                return tabDesignSanction;
        }
        return null;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    @Override
    public int getCount() {
        return tabNumber;
    }
}