package com.oodi.jingoo.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 6/2/17.
 */

public class AnalyticsHeader implements Serializable {

    public AnalyticsHeader(){}

    List<AnalyticsFooter> analyticsFooters ;
    String id ;
    String name ;

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

    public List<AnalyticsFooter> getAnalyticsFooters() {
        return analyticsFooters;
    }

    public void setAnalyticsFooters(List<AnalyticsFooter> analyticsFooters) {
        this.analyticsFooters = analyticsFooters;
    }
}
