package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 11/22/17.
 */

public class ChildReport implements Serializable {

    String product_id ;
    String products_name ;
    String products_size ;
    String category_name ;
    String selling_qty ;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getProducts_size() {
        return products_size;
    }

    public void setProducts_size(String products_size) {
        this.products_size = products_size;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSelling_qty() {
        return selling_qty;
    }

    public void setSelling_qty(String selling_qty) {
        this.selling_qty = selling_qty;
    }
}
