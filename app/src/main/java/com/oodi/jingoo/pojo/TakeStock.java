package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 6/14/17.
 */

public class TakeStock implements Serializable {

    public TakeStock(){}

    String id ;
    String category_id ;
    String products_name ;
    String products_desc ;
    String products_image ;
    String keywords ;
    String sort_order;
    String is_active ;
    String created_at;
    String last_activity_at ;
    String products_size ;
    String stock_qty , last_updated;

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getStock_qty() {
        return stock_qty;
    }

    public void setStock_qty(String stock_qty) {
        this.stock_qty = stock_qty;
    }

    public String getProducts_size() {
        return products_size;
    }

    public void setProducts_size(String products_size) {
        this.products_size = products_size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getProducts_desc() {
        return products_desc;
    }

    public void setProducts_desc(String products_desc) {
        this.products_desc = products_desc;
    }

    public String getProducts_image() {
        return products_image;
    }

    public void setProducts_image(String products_image) {
        this.products_image = products_image;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLast_activity_at() {
        return last_activity_at;
    }

    public void setLast_activity_at(String last_activity_at) {
        this.last_activity_at = last_activity_at;
    }

}
