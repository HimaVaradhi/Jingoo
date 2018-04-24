package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 2/3/18.
 */

public class CSM implements Serializable {

    String id ;
    String points ;
    String name ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
