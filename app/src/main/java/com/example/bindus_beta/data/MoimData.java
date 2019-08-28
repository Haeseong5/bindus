package com.example.bindus_beta.data;


import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class MoimData {
    ArrayList<String> UIDs;
    ArrayList<String> category;
    Timestamp date;
    String date_str;
    String imgURL1;
    int index;
    String intro;
    int isDeleted;
    String leader;
    String location;
    String moimID;
    String place;
    String title;
    int price;
    String docID;
    ArrayList<String> placeimg;

    public ArrayList<String> getPlaceimg() {
        return placeimg;
    }

    public void setPlaceimg(ArrayList<String> placeimg) {
        this.placeimg = placeimg;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public MoimData(){ }

    public void setUIDs(ArrayList<String> UIDs) {
        this.UIDs = UIDs;
    }

    public int getIndex() {
        return index;
    }


    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getDate_str() {
        return date_str;
    }

    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }

    public ArrayList<String> getUIDs() {
        return UIDs;
    }

    public String getImgURL1() {
        return imgURL1;
    }

    public void setImgURL1(String imgURL1) {
        this.imgURL1 = imgURL1;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMoimID() {
        return moimID;
    }

    public void setMoimID(String moimID) {
        this.moimID = moimID;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}