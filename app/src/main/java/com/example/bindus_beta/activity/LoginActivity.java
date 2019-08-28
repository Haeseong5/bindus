package com.example.bindus_beta.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bindus_beta.R;
import com.example.bindus_beta.data.UserData;
import com.example.bindus_beta.helper.FireBaseApi;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

import static com.example.bindus_beta.helper.FireBaseApi.firebaseAuth;

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    FirebaseUser mFireBaseUser;
    private FirebaseAuth mFirebaseAuth;

    EditText etEmail;
    EditText etPassword;
    Button btLoginEmail;
    Button btLoginFacebook;
    TextView tvSignUp;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.login_email_edit);
        etPassword = findViewById(R.id.login_password_edit);
        btLoginEmail = findViewById(R.id.login_email_button);
        btLoginFacebook = findViewById(R.id.login_facebook_button);
        tvSignUp = findViewById(R.id.login_signUp);
        setClickListener();
        mFirebaseAuth = firebaseAuth;
        mFireBaseUser = FireBaseApi.firebaseUser;

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        mFireBaseUser = firebaseAuth.getCurrentUser();
                        if (mFireBaseUser != null) {
//                    Log.d(TAG, "onAuthStateChanged:signed_in");
                            println("onAuthStateChanged:signed_in");
                            FireBaseApi.getCurrentToken(firebaseAuth.getCurrentUser().getUid()); //db에 토큰저장

                        } else {
                            // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                            println("onAuthStateChanged:signed_out");
                        }
                        // ...
                    }
                };
                // ...
            }
        };
    }//END onCreate();

    protected void setClickListener()
    {
        btLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                println("이메일 로그인");
                emailLogin(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
        btLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                println("회원가입하기");
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    emailLogin(etEmail.getText().toString(), etPassword.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void emailLogin(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "로그인 실패...", Toast.LENGTH_SHORT).show();
                        } else {//로그인 성공
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            println("로그인 되었습니다.(email login success)");
                            finish();
                        }
                    }
                });
    }

    private void facebookLogin(){
        // LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
        mCallbackManager = CallbackManager.Factory.create(); //로그인 응답을 처리할 콜백 관리자
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
        { //로그인 결과에 응답하려면 LoginManager 또는 LoginButton에 콜백을 등록해야 합니다.
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("onSuccess", "onSuccess");
                //로그인에 성공하면 LoginResult 매개변수에 새로운 AccessToken과 최근에 부여되거나 거부된 권한이 포함됩니다.
                //로그인 상태 확인
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = (accessToken != null) && (!accessToken.isExpired()); //액세스토큰이 null이 아니고 만료되지 않았다면
                handleFacebookAccessToken(accessToken,isLoggedIn);

            }

            @Override
            public void onCancel() {
                Log.e("onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("onError", "onError " + exception.getLocalizedMessage());
            }
        });
    }

    private void handleFacebookAccessToken(final AccessToken accessToken, final boolean isLoggedIn) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            if(isLoggedIn == true)
                            {
                                mFireBaseUser = mFirebaseAuth.getCurrentUser();
//                                FireBaseApi.getCurrentToken(mFireBaseUser.getUid()); //db에 토큰저장
                                //프로그래스바 추가
                            }
                        } else {
                            println("파이어베이스 로그인 실패");
                        }
                    }
                });
    }

    private void autoLogin(FirebaseUser currentUser){
        if (currentUser != null) {
            // User is signed in
            println("auto login : "+currentUser.getEmail());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
//            checkUserDetailInfo(); //느림. 이 경우에는 메인에서 잡을 수 있도록 해씀.
//            FireBaseApi.getCurrentToken(mFireBaseUser.getUid()); //db에 토큰저장, 초기회원은 에러뜸
            finish();
        } else {
            // No mFirebaseUser is signed in
        }
    }
    protected void checkUserDetailInfo(){ //상세정보가 등록되어있는 지 확인
        FireBaseApi.readFireStore("user",mFireBaseUser.getUid(),
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserData userData = documentSnapshot.toObject(UserData.class);
                        if(userData!=null){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            println("로그인 되었습니다. (check user info)");
                            finish();
                        }else{
                            Intent intent = new Intent(LoginActivity.this, RegistProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        마지막으로 onActivityResult 메서드에서 callbackManager.onActivityResult를 호출하여
//        로그인 결과를 callbackManager를 통해 LoginManager에 전달합니다.
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if mFirebaseUser is signed in (non-null) and update UI accordingly.
        // 사용자가 로그인 되어있는 지 확인 -> 자동로그인으로 현재 유저 객체 보냄
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        FireBaseApi.firebaseUser = firebaseAuth.getCurrentUser();
//        println("_Login onStart :  "+currentUser.getUid());
        autoLogin(FireBaseApi.firebaseUser);
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        //액티비티 애니메이션 x
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
