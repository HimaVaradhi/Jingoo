package com.oodi.jingoo.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 7/24/17.
 */

public class Child implements Serializable {

    String products_name ;
    String value1 ;
    String value2 ;
    String value3 ;
    String new_price ;

    String id;
    String category_id;
    String company_id;
    String brand_id;
    String products_size ;
    String state ;
    String product_price  ;

    List<AnalyticsFooter> analyticsFooters ;

    public List<AnalyticsFooter> getAnalyticsFooters() {
        return analyticsFooters;
    }

    public void setAnalyticsFooters(List<AnalyticsFooter> analyticsFooters) {
        this.analyticsFooters = analyticsFooters;
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

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getProducts_size() {
        return products_size;
    }

    public void setProducts_size(String products_size) {
        this.products_size = products_size;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

}
