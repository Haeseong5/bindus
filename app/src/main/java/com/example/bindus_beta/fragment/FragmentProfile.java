package com.example.bindus_beta.fragment;

import androidx.fragment.app.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.bindus_beta.R;
import com.example.bindus_beta.activity.LeaderActivity;
import com.example.bindus_beta.activity.LoginActivity;
import com.example.bindus_beta.activity.PlaceActivity;
import com.example.bindus_beta.adapter.ProfileAdapter;
import com.example.bindus_beta.data.ProfileItem;
import com.example.bindus_beta.helper.FireBaseApi;
import com.example.bindus_beta.helper.GlobalUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static com.example.bindus_beta.activity.MainActivity.dismissDialog;
import static com.example.bindus_beta.activity.MainActivity.showDialog;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentProfile extends Fragment {
    private String TAG = "FragmentProfile";
    private static FragmentProfile instance = null;
    public static int CAMERA_REQUEST = 100;
    FirebaseUser mFireBaseUser;
    private StorageReference mStorageRef;
    GlobalUser globalUser;
    View rootView;
    TextView tvName;
    TextView tvSchool;
    TextView tvGrade;
    TextView tvMyPosts;
    ImageView ivProfileImage;
    ListView listView1;
    ListView listView2;
    ImageView imageView;
    ScrollView scrollView;
    TextView tvHash;

    ArrayList<String> badge;
    ProfileAdapter profileAdapter;
    FirebaseFirestore db;
    String name;
    String email;
    String image;
    Bitmap bitmap;
    HashDialog hashDialog;
    String hashTag;
    public static FragmentProfile getInstance(){
        if(instance == null){
            synchronized (FragmentProfile.class){
                if(instance == null){
                    instance = new FragmentProfile();
                }
            }
        }
        return instance;
    }

    @Nullable
    @Override //Fragment가 자신의 UI를 그릴 때 호출합니다. UI를 그리기 위해 메서드에서는 View를 Return 해야 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        tvName = rootView.findViewById(R.id.profile_name_text);
        ivProfileImage = rootView.findViewById(R.id.profile_image);
        tvSchool = rootView.findViewById(R.id.profile_school_text);
        tvHash = rootView.findViewById(R.id.profile_hashtag);
        listView1 = rootView.findViewById(R.id.profile_listview);
        scrollView = rootView.findViewById(R.id.profile_scrollview);
        listView2 = rootView.findViewById(R.id.profile_listview2);
        mFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        globalUser = GlobalUser.getInstance();
        badge = new ArrayList<>();
        badge.add("https://firebasestorage.googleapis.com/v0/b/test-38218.appspot.com/o/category%2Fbadge_img%2FKakaoTalk_20190823_002551285.png?alt=media&token=bb642e1f-9d97-49db-aa17-815ec99dd5ea");
        badge.add("https://firebasestorage.googleapis.com/v0/b/test-38218.appspot.com/o/category%2FBADGE_img%2Fcalligraphy.png?alt=media&token=fa6e8359-02f0-4979-90fa-b29ed00fd840");

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CAMERA_REQUEST);
            }
        });



        setProfile();
//        checkUserDetailInfo();
        setMyInfoList();
        setCustomerCenterList();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the mFireBaseUser *asynchronously* -- don't block
                // this thread waiting for the mFireBaseUser's response! After the mFireBaseUser
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        return rootView;
    }
    protected void setProfile(){
//        println("nick:"+globalUser.getNickName());
        tvName.setText(globalUser.getName());

        tvHash.setText(globalUser.getStHashTag());
        Log.d("profile",(globalUser.getStHashTag()));

        if(globalUser.getImg()!=null){
            Glide.with(getActivity()).load(globalUser.getImg()).into(ivProfileImage);
            Log.d("profile",globalUser.getImg());
        }
    }
//
//
    void setHashTag(){
        //파라미터에 리스너 등록
        hashDialog = new HashDialog(getActivity(),tvHash.getText().toString(),positiveListener,negativeListener);
        hashDialog.show();
    }

    private View.OnClickListener positiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "확인버튼이 눌렸습니다.",Toast.LENGTH_SHORT).show();
//            tvHash.setText();
            hashDialog.dismiss();
        }
    };

    private View.OnClickListener negativeListener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getActivity(), "취소버튼이 눌렸습니다.",Toast.LENGTH_SHORT).show();
            hashDialog.dismiss();
        }
    };

    protected void setMyInfoList() {
        ArrayList<String> menus = new ArrayList<>();
        menus.add("마이프로필");
        menus.add("해시태그로 나를 소개하기");
        menus.add("내가 참여하고 있는 모임");
        menus.add("바인더스 리더 신청하기");
        menus.add("공간 등록하기");

        profileAdapter = new ProfileAdapter(getActivity(), menus);
        listView1.setAdapter(profileAdapter);
        listViewHeightSet(profileAdapter, listView1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),position,Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        break;
                    case 1:
                        setHashTag();
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent intent = new Intent(getActivity(), LeaderActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), PlaceActivity.class);
                        startActivity(intent);
                        break;

                }

            }
        });
        listView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        });
    }

    protected void setCustomerCenterList(){
        ArrayList<String> profileItems2 = new ArrayList<>();
        profileItems2.add("친구에게 바인더스 소개하기");
        profileItems2.add("공지사항 / 이벤트");
        profileItems2.add("도움말");
        profileItems2.add("문의하기");
        profileItems2.add("로그아웃");

        profileAdapter = new ProfileAdapter(getActivity(), profileItems2);
        listView2.setAdapter(profileAdapter);
        listViewHeightSet(profileAdapter,listView2);
        listView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "취향이 통하는 용감한 사람들의 모임, 바인더스 ");
                        intent.putExtra(Intent.EXTRA_TEXT, "플레이스토어에 바인더스를 검색해보세요!!!");

                        Intent chooser = Intent.createChooser(intent, "공유");
                        startActivity(chooser);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        FireBaseApi.firebaseAuth.signOut();
                        getActivity().finish();
                        break;
                }
            }
        });
    }
//
//
//
//    void println(String message){
//        Log.d(TAG, message);
//        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
//    }
//
    private void listViewHeightSet(Adapter listAdapter, ListView listView){
        //        스크롤뷰안에 리스트뷰가 있어서 리스트뷰 높이값 계산 필요.
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    protected void updateProfilePhoto() {
        showDialog();
        StorageReference profileRef = mStorageRef.child("user/" ).child(mFireBaseUser.getUid() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getActivity(), "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                dismissDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getPhotoUri();
                Toast.makeText(getActivity(), "사진 업로드가 잘 됐습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void writePhoto(String uri){
        DocumentReference washingtonRef = db.collection("user").document(FireBaseApi.firebaseUser.getUid());

// Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("img", uri)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Glide.with(getActivity()).load(uri).into(ivProfileImage);
                        dismissDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    public void getPhotoUri(){
        FireBaseApi.getUriStorage(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri photoUri = uri;
                writePhoto(photoUri.toString());
//                Log.d("success"+photoUri);
                globalUser.setImg(photoUri.toString());
            }
        },new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
//                println("getPhotoUri Failure");
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED){
            if (requestCode == CAMERA_REQUEST) {
                Uri image = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                    updateProfilePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
