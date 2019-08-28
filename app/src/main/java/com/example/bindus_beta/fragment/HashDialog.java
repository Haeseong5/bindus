package com.example.bindus_beta.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.bindus_beta.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;

public class HashDialog extends Dialog implements View.OnClickListener {
    Context context;
    private Button mPositiveButton;
    private Button mNegativeButton;
    ChipsInput chipsInput;
    Chip chip;
    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;
    EditText editText;
    String hashTag;
    private CustomDialogListener customDialogListener;

    //인터페이스 설정
    interface CustomDialogListener{
        void onPositiveClicked(String tag);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hashtag);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;

        getWindow().setAttributes(layoutParams);

        editText = findViewById(R.id.hashtag_edit);
        //셋팅
//        chip = findViewById(R.id.chip);
        mPositiveButton=(Button)findViewById(R.id.pbutton);
        mNegativeButton=(Button)findViewById(R.id.nbutton);
        editText.setText(hashTag);
        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);

    }

    //생성자 생성
    public HashDialog(@NonNull Context context, String hashTag, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        this.context = context;
        this.hashTag = hashTag;
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pbutton: //확인 버튼을 눌렀을 때
                //각각의 변수에 EidtText에서 가져온 값을 저장
                String tag = editText.getText().toString();

                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                customDialogListener.onPositiveClicked(tag);
                dismiss();
                break;
            case R.id.nbutton: //취소 버튼을 눌렀을 때
                cancel();
                break;
        }
    }

}


