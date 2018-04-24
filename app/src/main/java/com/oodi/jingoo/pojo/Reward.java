package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 2/5/18.
 */

public class Reward implements Serializable {

    String id;
    String item_name;
    String item_points ;
    String item_image ;
    String item_stock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_points() {
        return item_points;
    }

    public void setItem_points(String item_points) {
        this.item_points = item_points;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_stock() {
        return item_stock;
    }

    public void setItem_stock(String item_stock) {
        this.item_stock = item_stock;
    }
}
