package com.example.bindus_beta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bindus_beta.R;
import com.example.bindus_beta.activity.MoimActivity;
import com.example.bindus_beta.adapter.CategoryAdapter;
import com.example.bindus_beta.data.CategoryData;
import com.example.bindus_beta.data.MoimData;
import com.example.bindus_beta.helper.FireBaseApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentCategory extends Fragment {
    private static FragmentCategory instance = null;
    View rootView;
    public static FragmentCategory getInstance(){
        if(instance == null){
            synchronized (FragmentCategory.class){
                if(instance == null){
                    instance = new FragmentCategory();
                }
            }
        }
        return instance;
    }

    private String TAG = "Category activity";
    GridView listView;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryData> categoryList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        listView = rootView.findViewById(R.id.category_list);
        categoryList = new ArrayList<>();

        CategoryData categoryData = new CategoryData();
        categoryData.setEngName("all view");
        categoryData.setKorName("모두보기");

        categoryList.add(categoryData);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
        setCategory();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MoimActivity.class);
                intent.putExtra("category",categoryList.get(position).getEngName());
                startActivity(intent);
            }
        });


        return rootView;
    }
    void setCategory(){
        FireBaseApi.firestore.collection("category")
//                .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                CategoryData categoryData = document.toObject(CategoryData.class);
                                if(categoryData!=null){
                                    categoryList.add(categoryData);
                                }
                            }
                            listView.setAdapter(categoryAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    @Override
    public void onResume() {
        super.onResume();
    }
}
