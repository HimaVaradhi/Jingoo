package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 3/4/17.
 */

public class StoreList implements Serializable {

    public StoreList(){}

    String id , name ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
