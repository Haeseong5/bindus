package com.example.bindus_beta.data;

import com.example.bindus_beta.adapter.CategoryAdapter;

public class CategoryData {
    String badge_img;
    String engName;
    String korName;
    String img;
    public CategoryData(){}
    public CategoryData(String badge_img, String engName, String korName, String img) {
        this.badge_img = badge_img;
        this.engName = engName;
        this.korName = korName;
        this.img = img;
    }

    public String getBadge_img() {
        return badge_img;
    }

    public void setBadge_img(String badge_img) {
        this.badge_img = badge_img;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getKorName() {
        return korName;
    }

    public void setKorName(String korName) {
        this.korName = korName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
