package com.oodi.jingoo.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by pc on 6/7/17.
 */

public class Banners implements Serializable {

    int w1 ;
    Bitmap b ;

    public Banners() {
    }

    String id;
    String banner_name;
    String banner_image ;
    String link ;

    public Bitmap getB() {
        return b;
    }

    public void setB(Bitmap b) {
        this.b = b;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getW1() {
        return w1;
    }

    public void setW1(int w1) {
        this.w1 = w1;
    }
}
