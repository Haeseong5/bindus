package com.example.bindus_beta.activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import com.example.bindus_beta.R;
import com.example.bindus_beta.data.UserData;
import com.example.bindus_beta.helper.FireBaseApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;

//로그인 했을 때
public class RegistProfileActivity extends AppCompatActivity {
    FirebaseUser mFirebaseUser;
    StorageReference mStorageRef;

    EditText etName;
    Button btBirthday;
    TextView tvBirthday;
    RadioButton rbMan;
    RadioButton rbWoman;
    RadioGroup radioGroup;
    Button btFinish;
    String sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        etName = findViewById(R.id.regist_profile_name);
        btBirthday = findViewById(R.id.regist_date_button);
        rbMan = findViewById(R.id.regist_radio_man);
        rbWoman = findViewById(R.id.regist_radio_woman);
        btFinish = findViewById(R.id.regist_profile_ok_button);
        tvBirthday = findViewById(R.id.regist_birthday_text);
        radioGroup = findViewById(R.id.regist_radio_group);
        mFirebaseUser = FireBaseApi.firebaseUser;
        mStorageRef = FireBaseApi.storageReference;


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.regist_radio_man){
                    sex = "male";
                    println("male");
                }else{
                    sex = "female";
                    println("female");
                }
            }
        });
        btBirthday.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(RegistProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        String msg = String.format("%d-%d-%d", year, month+1, date);
                        Toast.makeText(RegistProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                        tvBirthday.setText(msg);
                    }
                }, 1999, 1, 1);
                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = etName.getText().toString();
                String inputBirthday = tvBirthday.getText().toString();
                if(mFirebaseUser != null){

                    if(inputName.length() >= 2 && inputName.length() <= 10 ){
                        //1.firestore에 유저 firestore 추가
                        createUser_fireStore(inputName,sex,inputBirthday);
                    } else{
                        etName.getText().clear();
                        Toast.makeText(RegistProfileActivity.this,"2글자 이상 10글자 이하입니다.",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }//onCreate

    protected  void createUser_fireStore(String name, String gender, String birthday){
        //등록된 정보가 없으면 로그인화면에서 프로필정보 등록화면으로 넘어온다.
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserData userData = new UserData();
        userData.setEmail(firebaseUser.getEmail());
        userData.setName(name);
        userData.setGender(gender);
        userData.setBirthDay(birthday);

        db.collection("user").document(firebaseUser.getUid()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistProfileActivity.this,"DB에 회원 등록 성공.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistProfileActivity.this,"DB에 회원 등록 실패",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void println(String message){
        Log.d("UpdateActivity_print", message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}