package com.example.bindus_beta.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.example.bindus_beta.R;

import java.util.ArrayList;

public class DetailAdapter extends PagerAdapter {
    ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    int a;
    DetailAdapter(){

    }
    public DetailAdapter(Context context, ArrayList<String> images, int a){
        this.context = context;
        this.images = images;
        this.a = a;
        Log.d("imagesAdapter", String.valueOf(images.size()));
    }

    @Override
    public int getCount() {
        return images.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_detail, container, false);
        ImageView imageView = v.findViewById(R.id.item_detail_image);
//        Toast.makeText(context, images.get(position),Toast.LENGTH_SHORT).show();
        Log.d("imagesAdapter",  images.get(position));

        Glide.with(context).load(images.get(position) ).into(imageView);
        container.addView(v) ;

        return v;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
//        container.invalidate();
    }

}