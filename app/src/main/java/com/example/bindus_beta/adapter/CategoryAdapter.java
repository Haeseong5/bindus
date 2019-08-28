package com.example.bindus_beta.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bindus_beta.R;
import com.example.bindus_beta.data.CategoryData;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryData> categoryList;
    public CategoryAdapter (Context context, ArrayList<CategoryData> categoryList){
        this.context = context;
        this.categoryList = categoryList;
    }
    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_category, null);
        }
        ImageView imageView = convertView.findViewById(R.id.item_category_image);
        TextView textView = convertView.findViewById(R.id.item_category_text);
        Glide.with(context).load(categoryList.get(position).getBadge_img()).into(imageView);
        textView.setText(categoryList.get(position).getKorName());


        return convertView;
    }
}
