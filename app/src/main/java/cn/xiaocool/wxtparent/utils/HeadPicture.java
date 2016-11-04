package cn.xiaocool.wxtparent.utils;

/**
 * Created by mac on 16/1/26.
 */

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;

import cn.xiaocool.wxtparent.head.picture.Util;

@SuppressLint("SdCardPath")
public class HeadPicture {
    private String filepath = "/sdcard/myheader";
    private String filepathimg = "";
    private String picname = "newpic";

    public void getHeadPicture(ImageView head) {
        filepathimg = filepath + "/" + picname + ".jpg";
        File f = new File(filepathimg);
        if (f.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(filepathimg);
            head.setImageBitmap(Util.toRoundBitmap(bm));
        }
    }
}
