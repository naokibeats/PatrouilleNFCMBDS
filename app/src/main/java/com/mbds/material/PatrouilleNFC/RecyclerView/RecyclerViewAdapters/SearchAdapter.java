package com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mbds.material.PatrouilleNFC.RecyclerView.RecyclerViewClasses.Search;
import com.mbds.material.PatrouilleNFC.Utils.PicassoTransform.CircleTransform;
import com.naokistudio.material.PatrouilleNFC.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private ArrayList<Search> searches;
    Context context;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
        }
    };


    public SearchAdapter(Context context, ArrayList<Search> searches) {
        this.searches = searches;
        this.context = context;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final TextView textViewTitle = (TextView) holder.view.findViewById(R.id.textViewItemTitle);
        final TextView textViewContent = (TextView) holder.view.findViewById(R.id.textViewItemContent);
        final ImageView imageViewImage = (ImageView) holder.view.findViewById(R.id.imageViewImage);
        textViewTitle.setText(searches.get(position).getTitle());
        textViewContent.setText(searches.get(position).getExcerpt());
        Picasso.with(context).load(searches.get(position).getImage())
                .placeholder(holder.view.getResources()
                        .getDrawable(R.drawable.ic_contact_icon)).transform(new CircleTransform()).into(imageViewImage);
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
