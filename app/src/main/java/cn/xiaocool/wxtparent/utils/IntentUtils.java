package cn.xiaocool.wxtparent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mac on 16/1/23.
 */
public class IntentUtils {
    @SuppressWarnings("rawtypes")
    public static Intent getIntent(Activity activity, Class clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        return intent;
    }

    public static Intent getIntents(Context mContext, Class<?> clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
        return intent;
    }

}