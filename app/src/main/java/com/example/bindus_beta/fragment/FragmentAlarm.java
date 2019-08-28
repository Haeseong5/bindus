package com.example.bindus_beta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bindus_beta.R;

public class FragmentAlarm extends Fragment {
    private static FragmentAlarm instance = null;
    View rootView;
    public static FragmentAlarm getInstance(){
        if(instance == null){
            synchronized (FragmentAlarm.class){
                if(instance == null){
                    instance = new FragmentAlarm();
                }
            }
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
//        ImageView imageView = rootView.findViewById(R.id.)
        return rootView;
    }


}
