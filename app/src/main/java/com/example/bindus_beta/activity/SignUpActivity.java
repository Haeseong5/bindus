package com.example.bindus_beta.activity;

import android.content.Intent;
import android.os.Bundle;


import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bindus_beta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    static private String TAG = SignUpActivity.class.getSimpleName();
    EditText etMyEmail;
    EditText etPassword;
    EditText etCheckPW;
    Button btSignUpOk;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setView();
        mAuth = FirebaseAuth.getInstance();

        btSignUpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!isValidEmail()){
                        println("이메일 형식이 올바르지 않습니다.");
                    }else if(!isValidPassword()){
                        println("비밀번호 형식이 올바르지 않습니다.");
                    }else if(!isEqualPassword()){
                        println("비밀번호가 같지 않습니다.");
                    }else{
                        emailSignUp(etMyEmail.getText().toString(),etPassword.getText().toString());
                    }
                }catch (Exception E){
                    E.printStackTrace();
                    println("Exception");
                }
            }
        });

    }//end onCreate

    public void emailSignUp(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            println("회원가입 실패:"+task.isSuccessful());
                            etMyEmail.getText().clear();
                            etPassword.getText().clear();
                            etCheckPW.getText().clear();
                        }else{
                            //회원가입 성공
                            println("회원가입 성공");
                            finish();
                        }
                    }
                });
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        String email = etMyEmail.getText().toString();
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword() {
        String password = etPassword.getText().toString();
        final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$"); //비밀번호 정규식
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }
    private boolean isEqualPassword(){
        String password = etPassword.getText().toString();
        String checkPw = etCheckPW.getText().toString();
        if(password.equals(checkPw)){
            return true;
        }else{
            return false;
        }
    }

    private void setView(){
        etMyEmail = findViewById(R.id.signup_email);
        etPassword = findViewById(R.id.signup_password);
        btSignUpOk = findViewById(R.id.signup_button);
        etCheckPW = findViewById(R.id.signup_password_check);
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

}//end Class
