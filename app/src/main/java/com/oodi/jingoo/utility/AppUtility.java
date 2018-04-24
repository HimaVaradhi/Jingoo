package com.oodi.jingoo.utility;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by pc on 12/16/16.
 */

public class AppUtility {

    private Context mContext;

    public AppUtility(Context mContext) {
        this.mContext = mContext;
        setWindowMode();
    }


    public int compareVersionNames(String oldVersionName, String newVersionName) throws Exception {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i ++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length)?1:-1;
        }

        return res;
    }

    public void setWindowMode(){
        ((Activity)mContext).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

}
