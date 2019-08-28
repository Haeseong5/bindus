package com.example.bindus_beta.helper;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bindus_beta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FireBaseApi { //Util or Helper 디자인 패턴
    private static String TAG = "Firebase API";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static FireBaseApi instance = null;

    public static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public static FireBaseApi getInstance(){
        if(instance == null){
            synchronized (FireBaseApi.class){
                if(instance == null){
                    instance = new FireBaseApi();
                }
            }
        }
        return instance;
    }

    public static void setFcmToken(String uid, final String token){
        DocumentReference userRef = firestore.collection("user").document(uid);


        userRef
                .update("token", token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "setToken Success : " + token);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "setToken Error: ", e);
                    }
                });
    }

    public static void sendNotification(final Context context, final String regToken, final String title, final String messsage){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... parms) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", messsage);
                    dataJson.put("title", title);
                    json.put("notification", dataJson);
                    json.put("to", regToken); //메세지를 받아야하는 사람의 토큰
                    RequestBody body = RequestBody.create(JSON, json.toString());
//                    Log.d("server key: ",R..fcm_server_key);

                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + context.getResources().getString(R.string.fcm_server_key))
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.d("finalResponse", finalResponse);

                }catch (Exception e){
                    Log.d("error", e+"");
                }
                return  null;
            }
        }.execute();
    }
    public static void getCurrentToken(final String uid){ //현재토큰검색
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        GlobalUser globalUser = GlobalUser.getInstance();
                        globalUser.setToken(token);
                        Log.d("current token", token);
                        FireBaseApi.setFcmToken(uid,token);
                    }
                });

    }

    public static void readFireStore(String collection, String doc,
                                     OnSuccessListener<DocumentSnapshot> onSuccessListener){
        DocumentReference docRef = FireBaseApi.firestore.collection(collection).document(doc);
        docRef.get().addOnSuccessListener(onSuccessListener);
    }
    public static void deleteFireStore(String collection, String doc,
                                       OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        firestore.collection(collection).document(doc)
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
    public static void getUriStorage(OnSuccessListener<Uri> onSuccessListener, OnFailureListener onFailureListener){
        final StorageReference profileRef = storageReference.child("user/").child(firebaseUser.getUid()+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public static void deleteArrayFieldFireStore(String collection, String doc, final String field){
        DocumentReference myPostRef = firestore.collection(collection).document(doc);
        final GlobalUser globalUser = GlobalUser.getInstance();
// Set the "isCapital" field of the city 'DC'
        myPostRef.update("posts", FieldValue.arrayRemove(field))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        globalUser.getPosts().remove(field);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    public static void updateArrayFieldFireStore(String collection, String doc, final String field){
        DocumentReference myPostRef = firestore.collection(collection).document(doc);
// Set the "isCapital" field of the city 'DC'
        myPostRef.update("posts", FieldValue.arrayUnion(field))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        GlobalUser.getInstance().getPosts().add(field);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public static void uploadLocalFileStorage(ArrayList<Uri> images) {
        for(int i=0; i<images.size(); i++) {
            Uri file = Uri.fromFile(new File(images.get(i).toString()));
            StorageReference ref = storageReference.child("images/"+file.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(file);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }

    }
}
