package com.example.bindus_beta.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Dictionary;

public class MoimActivity extends AppCompatActivity {
    private String TAG = "Moim activity";
    private RecyclerView mRecyclerView;
    MoimAdapter moimAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MoimData> moimDataSet;
    TextView tvTotal;
    String category;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim);
        mRecyclerView = findViewById(R.id.market_recycler_view);
        tvTotal = findViewById(R.id.market_total_text);
        if(getIntent() != null){
            category = getIntent().getStringExtra("category");
        }
        moimDataSet = new ArrayList<>();
        moimAdapter = new MoimAdapter(getApplicationContext(), moimDataSet);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        readMarketData();
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MoimData moimData = moimDataSet.get(position);
                Intent intent = new Intent(MoimActivity.this, MoimDetailActivity.class);
                intent.putExtra("docID",moimData.getDocID());
                startActivityForResult(intent,123);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
    protected void readMarketData(){ //갯수한정해야함. 페이징?기법 찾아보기
        FireBaseApi.firestore.collection("moim")
                .orderBy("date", Query.Direction.DESCENDING)
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
                                    if(!category.equals("all view")){
                                        for(int i=0; i<moimData.getCategory().size(); i++){
                                            if(moimData.getCategory().get(i).equals(category)){
                                                moimDataSet.add(moimData); //카테고리가 맞는 경우만 데이터 추가
                                            }
                                        }
                                    }else{
                                        moimDataSet.add(moimData); //카테고리가 전체보기이면 데이터 싹다 추가
                                    }

                                    Log.d("post", String.valueOf(moimDataSet.size()));
                                }
                            }
                            mRecyclerView.setAdapter(moimAdapter);
                            tvTotal.setText("개설된 모임: " + moimDataSet.size());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        moimAdapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
