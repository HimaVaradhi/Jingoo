package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 6/13/17.
 */

public class Team implements Serializable {

    String id , username , phone , store_id;

    String email ;
    String store_name , avatar;

    String role_selling_price ;
    String role_transfer_stock ;
    String role_reports ;

    public String getRole_reports() {
        return role_reports;
    }

    public void setRole_reports(String role_reports) {
        this.role_reports = role_reports;
    }

    public String getRole_selling_price() {
        return role_selling_price;
    }

    public void setRole_selling_price(String role_selling_price) {
        this.role_selling_price = role_selling_price;
    }

    public String getRole_transfer_stock() {
        return role_transfer_stock;
    }

    public void setRole_transfer_stock(String role_transfer_stock) {
        this.role_transfer_stock = role_transfer_stock;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
