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
        /*tv_share = (RelativeLayout) findViewById(R.id.share);
        tv_share.setOnClickListener(this);*/
    }


    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM, 0, 0);
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

    /**
     * 微信分享图片
     */
    private void shareWX() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.parent_erweima);
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = "智校园";
        msg.description = "一款家园共育校园管理应用";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo_wx);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img";
        req.message = msg;
        req.scene = flag==0? SendMessageToWX.Req.WXSceneSession: SendMessageToWX.Req.WXSceneTimeline;
        WxtApplication wxtApplication = WxtApplication.getInstance();
        wxtApplication.api.sendReq(req);
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
                //showPopFormBottom(v);
                break;
        }
    }
}
