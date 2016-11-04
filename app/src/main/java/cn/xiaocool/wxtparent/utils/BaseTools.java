package cn.xiaocool.wxtparent.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by mac on 16/1/30.
 */
public class BaseTools {

    /** ��ȡ��Ļ�Ŀ�� */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}