package com.example.bindus_beta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.bindus_beta.R;

import java.util.ArrayList;

public class ProfileAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> menus;
    public ProfileAdapter(Context context, ArrayList menus){
        this.context = context;
        this.menus = menus;
    }
    @Override
    public int getCount() {
        return menus.size();
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
            convertView = layoutInflater.inflate(R.layout.item_profile, null);
        }
        TextView textView = convertView.findViewById(R.id.item_profile_text);
        textView.setText(menus.get(position));


        return convertView;    }
}
