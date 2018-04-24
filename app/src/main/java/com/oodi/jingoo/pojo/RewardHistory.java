package com.oodi.jingoo.pojo;

import java.io.Serializable;

/**
 * Created by pc on 2/5/18.
 */

public class RewardHistory implements Serializable {

    String id ;
    String type ;
    String points ;
    String note ;
    String created_at ;
    String refferel_status;
    String name;

    public String getRefferel_status() {
        return refferel_status;
    }

    public void setRefferel_status(String refferel_status) {
        this.refferel_status = refferel_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
