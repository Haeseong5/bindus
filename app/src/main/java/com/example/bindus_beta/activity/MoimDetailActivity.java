package com.example.bindus_beta.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.bindus_beta.R;
import com.example.bindus_beta.adapter.CommentAdapter;
import com.example.bindus_beta.adapter.DetailAdapter;
import com.example.bindus_beta.data.Comment;
import com.example.bindus_beta.data.MoimData;
import com.example.bindus_beta.helper.FireBaseApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.text.DateFormat.getDateTimeInstance;

public class MoimDetailActivity extends AppCompatActivity {
    ViewPager viewPager;
    Button btRegister;
    TextView tvTitle;
    TextView tvContent;
    TextView tvAddress;
    TextView tvPlace;
    TextView tvWrite;
    TextView tvTime;
    String docID;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ArrayList<Comment> commentList;
    LinearLayout writeLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_detail);
        tvTitle = findViewById(R.id.detail_tab_title);
        tvContent = findViewById(R.id.detail_content);
        tvAddress = findViewById(R.id.detail_address);
        tvPlace = findViewById(R.id.detail_place);
        btRegister = findViewById(R.id.detail_button);
        viewPager = findViewById(R.id.detail_viewpager);
        recyclerView = findViewById(R.id.review_recyclerview);
        tvWrite = findViewById(R.id.detail_reivew_write);
        tvTime = findViewById(R.id.detail_time);
//        writeLayout = findViewById(R.id.layout)
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList,getApplicationContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        if(getIntent()!=null){
            docID = getIntent().getStringExtra("docID");
            readMoimData();
            readComment_fireStore();
        }
        tvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"모임에 참여한 사람만 리뷰를 작성할 수 있습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"신청 되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void readComment_fireStore(){
        FireBaseApi.firestore.collection("moim").document(docID).collection("review")
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("comment", document.getId() + " => " + document.getData());
                                Comment comment = document.toObject(Comment.class);
                                if(comment != null){
                                    commentList.add(comment);
                                }
                                commentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("comment", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void readMoimData(){
        DocumentReference docRef = FireBaseApi.firestore.collection("moim").document(docID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                MoimData moimData = documentSnapshot.toObject(MoimData.class);
                int a = R.drawable.ic_logo;
                DetailAdapter detailAdapter = new DetailAdapter(getApplicationContext(),moimData.getPlaceimg(),a);
                viewPager.setAdapter(detailAdapter);
                tvTitle.setText(moimData.getTitle());
                long sec= moimData.getDate().getSeconds()*1000;
//                moimData.getDate().
                Log.d("timestamp", moimData.getDate().toString());
                Log.d("timestamp", moimData.getDate().toDate().toString());
                Log.d("timestamp", String.valueOf(moimData.getDate().toDate()));
                Log.d("timestamp", getTimeDate(sec));
//                Date date1 = new Date(String.valueOf(moimData.getDate()));
//
//                SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
//
//                String date22 =  datef.format(date1);
                tvTime.setText(getTimeDate(sec));

                tvContent.setText(moimData.getIntro());
                tvAddress.setText(moimData.getLocation());
                tvPlace.setText(moimData.getPlace());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentAdapter.notifyDataSetChanged();
    }


    public static String getTimeDate(long timestamp){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
//    public String MillToDate(long mills) {
//        String pattern = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//        String date = (String) formatter.format(new Timestamp(mills));
//        return date;
//    }


}
