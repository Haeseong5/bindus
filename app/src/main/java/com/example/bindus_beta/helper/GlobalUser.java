package com.example.bindus_beta.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class GlobalUser {
    private static String TAG = "println";
    private static GlobalUser instance = null;
    private String name;
    private String email;
    private String gender;
    private String token;
    private String birthday;
    private ArrayList<String> posts;
    private ArrayList<String> comments;
    private ArrayList<String> hashTag;
    private String img;
    String stHashTag;
    /**
     * 싱글턴, 하나의 객체만을 사용함.
     * @return
     */
    public static GlobalUser getInstance(){
        if(instance == null){
            synchronized (GlobalUser.class){
                if(instance == null){
                    instance = new GlobalUser();
                }
            }
        }
        return instance;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<String> getHashTag() {
        return hashTag;
    }

    public String getStHashTag() {
        return stHashTag;
    }

    public void setStHashTag(String stHashTag) {
        this.stHashTag = stHashTag;
    }

    public void setHashTag(ArrayList<String> hashTag) {
        this.hashTag = hashTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public static void println(Context context, String message){
        Log.d(TAG, message);
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}
