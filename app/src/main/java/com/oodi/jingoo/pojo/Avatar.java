package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 6/7/17.
 */

public class Avatar implements Serializable {

    public Avatar(){}

    String avatar ;
    boolean isSelected = false ;
    String id ;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
