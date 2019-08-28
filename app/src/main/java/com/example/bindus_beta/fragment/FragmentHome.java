package com.example.bindus_beta.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bindus_beta.R;
import com.example.bindus_beta.adapter.MoimAdapter;
import com.example.bindus_beta.data.MoimData;
import com.example.bindus_beta.helper.FireBaseApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.bindus_beta.activity.MainActivity.dismissDialog;

public class FragmentHome extends Fragment {
    private String TAG = "FragmentHome";
    private static FragmentHome instance = null;
    ImageView imageView;
    TextView textView;
    RecyclerView recyclerView;
    View rootView;
    MoimAdapter moimAdapter;
    ArrayList<MoimData>  moimDataArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageView imageView1;
    public static FragmentHome getInstance(){
        if(instance == null){
            synchronized (FragmentHome.class){
                if(instance == null){
                    instance = new FragmentHome();
                }
            }
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = rootView.findViewById(R.id.home_imageview);
        textView = rootView.findViewById(R.id.home_text);
        imageView1 = rootView.findViewById(R.id.home_image1);
        recyclerView = rootView.findViewById(R.id.home_recyclerview_moim);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(),2){
            @Override
            public boolean canScrollVertically() { // 세로스크롤 막기
                return false;
            }

            @Override
            public boolean canScrollHorizontally() { //가로 스크롤막기
                return false;
            }
        };
        recyclerView.setLayoutManager(mLayoutManager);
        moimDataArrayList = new ArrayList<>();
        moimAdapter = new MoimAdapter(getActivity(),moimDataArrayList);
        Glide.with(getActivity()).load("https://postfiles.pstatic.net/MjAxOTA4MjVfMzEg/MDAxNTY2NzQyNDQyMTky.qdTYOwlhAqPLMIlourKaiozsWypATiYMUgrM0H5vID0g.gHSxMFiX8PzLseJyH-O2CYpzZ0gbQSEYz6Y-RVsQZV4g.JPEG.g_sftdvp/vip_kit.jpg?type=w966")
                .into(imageView1);
        readMarketData();

        return rootView;
    }
    protected void readMarketData(){ //갯수한정해야함. 페이징?기법 찾아보기
        FireBaseApi.firestore.collection("moim")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                MoimData moimData = document.toObject(MoimData.class);
                                if(moimData != null){
                                    moimData.setDocID(document.getId());
                                        moimDataArrayList.add(moimData); //카테고리가 맞는 경우만 데이터 추가

                                    Log.d("moimDataArrayList", String.valueOf(moimDataArrayList.size()));
                                }
                            }
                            recyclerView.setAdapter(moimAdapter);
                            dismissDialog();

//                            tvTotal.setText("개설된 모임: " + moimDataSet.size());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
