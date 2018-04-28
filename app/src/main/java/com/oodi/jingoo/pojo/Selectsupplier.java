package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 9/12/17.
 */

public class Selectsupplier implements Serializable {

    String id ;
    String brand_name;
    String brand_image ;
    String is_selected ;
    boolean setSelected = false;
    String company_id ;

    public boolean isSetSelected() {
        return setSelected;
    }

    public void setSetSelected(boolean setSelected) {
        this.setSelected = setSelected;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public boolean isSelected() {
        return setSelected;
    }

    public void setSelected(boolean setSelected) {
        this.setSelected = setSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(String is_selected) {
        this.is_selected = is_selected;
    }
}
