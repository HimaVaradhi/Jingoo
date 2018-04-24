package com.oodi.jingoo.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 12/18/17.
 */

public class ReportHeader implements Serializable {

    List<ReportFooter> reportFooters ;
    String category_name ;

    public List<ReportFooter> getReportFooters() {
        return reportFooters;
    }

    public void setReportFooters(List<ReportFooter> reportFooters) {
        this.reportFooters = reportFooters;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
