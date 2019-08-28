package com.example.bindus_beta.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.bindus_beta.R;
import com.example.bindus_beta.data.MoimData;

import java.util.ArrayList;

public class MoimAdapter extends RecyclerView.Adapter<MoimAdapter.ViewHolder> {
    private ArrayList<MoimData> mDataset;
    Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView ivPhoto;
        public TextView tvTitle;
        public TextView tvPlace;
        public TextView tvLeader;

        public ViewHolder(View view) {
            super(view);
            ivPhoto = (ImageView)view.findViewById(R.id.market_item_image);
            tvTitle = (TextView)view.findViewById(R.id.market_item_title);
            tvPlace = view.findViewById(R.id.market_item_place);
            tvLeader = view.findViewById(R.id.market_item_leader);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoimAdapter(Context context, ArrayList<MoimData> moimDataset) {
        this.context = context;
        mDataset = moimDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MoimAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_moim, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvTitle.setText(mDataset.get(position).getTitle());
        holder.tvPlace.setText(mDataset.get(position).getPlace());
        holder.tvLeader.setText(mDataset.get(position).getLeader());
        Log.d("image check",mDataset.get(position).getImgURL1());
        Glide.with(context).load(mDataset.get(position).getImgURL1()).into(holder.ivPhoto);
//        holder.ivPhoto.setImageURI(Uri.parse(mDataset.get(position).getImageURL1()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}



