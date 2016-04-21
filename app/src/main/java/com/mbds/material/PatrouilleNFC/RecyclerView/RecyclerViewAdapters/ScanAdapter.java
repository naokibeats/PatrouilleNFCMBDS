package com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mbds.material.PatrouilleNFC.Model.Infraction;
import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses.Scan;
import com.mbds.material.PatrouilleNFC.Utils.PicassoTransform.CircleTransform;
import com.mbds.material.PatrouilleNFC.Utils.Utilitaire;
import com.naokistudio.material.PatrouilleNFC.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ViewHolder>{

    private List<Infraction> scans;
    Context context;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
        }
    };


    public ScanAdapter(Context context, List<Infraction> scans) {
        this.scans = scans;
        this.context = context;
    }

    @Override
    public ScanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final TextView textViewTitle = (TextView) holder.view.findViewById(R.id.textViewItemTitle);
        final TextView textViewContent = (TextView) holder.view.findViewById(R.id.textViewItemContent);
       // final ImageView imageViewImage = (ImageView) holder.view.findViewById(R.id.imageViewImage);
        textViewTitle.setText(scans.get(position).getLibelle());
        textViewContent.setText(Utilitaire.formatterDateString(scans.get(position).getDate_infraction()));

    }


    @Override
    public int getItemCount() {
        if(scans!=null)
        return scans.size();
        else
            return 0;    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
