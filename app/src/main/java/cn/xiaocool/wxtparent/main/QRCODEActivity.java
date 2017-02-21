package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;

import java.io.ByteArrayOutputStream;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.ui.SharePopupWindow;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.view.WxtApplication;

public class QRCODEActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout btn_exit;
    private RelativeLayout tv_share;
    private   int flag=0;
    private Context context;
    SharePopupWindow takePhotoPopWin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qrcode);
        context = this;
        initView();
    }

    private void initView() {
        btn_exit = (RelativeLayout) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        tv_share = (RelativeLayout) findViewById(R.id.share);
        tv_share.setOnClickListener(this);
    }


    public void showPopFormBottom() {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.TOP, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.ToastShort(context, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.ToastShort(context, "微信朋友圈");
                    history();
                    break;
            }
        }
    };

    private static int THUMB_SIZE = 100;
    /**
     * 微信分享图片
     */
    private void shareWX() {

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.parent_erweima);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);  // ��������ͼ

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        WxtApplication wxtApplication = WxtApplication.getInstance();
        wxtApplication.api.sendReq(req);
    }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    /**
     * 分享到微信好友
     */
    private void setting() {
        //ToastUtils.ToastShort(this, "分享到微信好友");
        flag = 0;
        shareWX();
        takePhotoPopWin.dismiss();

    }

    /**
     * 分享到微信朋友圈
     */
    private void history() {
        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
        flag = 1;
        shareWX();
        takePhotoPopWin.dismiss();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
                break;
            case R.id.share:
                showPopFormBottom();
                break;
        }
    }
}
