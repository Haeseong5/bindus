package com.example.bindus_beta.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.bindus_beta.R;
import com.example.bindus_beta.data.UserData;
import com.example.bindus_beta.fragment.FragmentCategory;
import com.example.bindus_beta.fragment.FragmentAlarm;
import com.example.bindus_beta.fragment.FragmentHome;
import com.example.bindus_beta.fragment.FragmentProfile;
import com.example.bindus_beta.helper.FireBaseApi;
import com.example.bindus_beta.helper.GlobalUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private final long FINISH_INTERVAL_TIME = 1000; //2초 안에 한번 더 누르면 종료
    private long   backPressedTime = 0;
    public static ProgressDialog progressDialog;
    GlobalUser globalUser;
    FragmentManager fragmentManager;     // Activity 내의 Fragment를 관리하기 위해서는 FragmentManager를 사용
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    FirebaseFirestore firestore;
    FirebaseUser mFireBaseUser;

    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setBottomNavigationView();

        context = this;
        progressDialog = new ProgressDialog(MainActivity.this);
        globalUser = GlobalUser.getInstance();
        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FireBaseApi.firestore;
        initGlobalUser();
        fragmentManager = getSupportFragmentManager();




        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //Fragment의 추가, 제거, 변경 등의 작업
        fragmentTransaction.add(R.id.fragment_container, FragmentHome.getInstance()).commit(); //첫 프래그먼트 지정
    } //end onCreate


    protected void setBottomNavigationView()
    {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation_view);
        // BottomNavigationView 메뉴를 선택할 때마다 위치가 변하지 않도록
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        replaceFragment(FragmentHome.getInstance());
                        break;
                    }
                    case R.id.navigation_menu2: {
                        replaceFragment(FragmentCategory.getInstance());
                        break;
                    }
                    case R.id.navigation_menu3: {
                        replaceFragment(FragmentAlarm.getInstance());
                        break;
                    }
                    case R.id.navigation_menu4: {
                        replaceFragment(FragmentProfile.getInstance());
                        break;
                    }
                }
                return true;
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }
    protected void initGlobalUser(){
        showDialog();
        DocumentReference docRef = firestore.collection("user").document(mFireBaseUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData userData = documentSnapshot.toObject(UserData.class);
                if(userData!=null){
                    globalUser.setName(userData.getName());
                    globalUser.setEmail(userData.getEmail());
                    globalUser.setBirthday(userData.getBirthDay());
                    globalUser.setGender(userData.getGender());
                    globalUser.setImg(userData.getImg());
                    globalUser.setHashTag(userData.getHashtag());
                    if (globalUser.getHashTag()!=null){
                        String stHashTag ="";
                        for(int i=0; i<globalUser.getHashTag().size();i++){
                            Log.d("hash",globalUser.getHashTag().get(i));
                                stHashTag += "#"+globalUser.getHashTag().get(i)+" ";
                        }
                        globalUser.setStHashTag(stHashTag);
                    }
//                    globalUser.setComments(userData.getComments());
                }else{
                    Intent intent = new Intent(MainActivity.this, RegistProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public static void showDialog(){
        //요청 이 다이어로그를 종료할 수 있게 지정함
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.show();
    }
    public static void dismissDialog(){
        progressDialog.dismiss();
    }
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
