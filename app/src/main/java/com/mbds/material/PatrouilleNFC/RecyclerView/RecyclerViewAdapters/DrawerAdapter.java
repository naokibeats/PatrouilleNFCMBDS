package com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewAdapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses.DrawerItem;
import com.naokistudio.material.PatrouilleNFC.R;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private ArrayList<DrawerItem> drawerItems;

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public ViewHolder(View view) {
            super(view);
        }
    }

    public DrawerAdapter(ArrayList<DrawerItem> drawerItems) {
        this.drawerItems = drawerItems;
    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextView title = (TextView) holder.itemView.findViewById(R.id.textViewDrawerItemTitle);
        ImageView icon = (ImageView) holder.itemView.findViewById(R.id.imageViewDrawerIcon);

        title.setText(drawerItems.get(position).getTitle());
        icon.setImageDrawable(drawerItems.get(position).getIcon());
    }


    @Override
    public int getItemCount() {
        return drawerItems.size();
    }
}


