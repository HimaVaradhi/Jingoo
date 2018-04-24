package com.oodi.jingoo.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pc on 6/12/17.
 */

public class NotificationHeader implements Serializable {

    ArrayList<NotificationFooter> notificationFooterList ;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<NotificationFooter> getNotificationFooterList() {
        return notificationFooterList;
    }

    public void setNotificationFooterList(ArrayList<NotificationFooter> notificationFooterList) {
        this.notificationFooterList = notificationFooterList;
    }
}
