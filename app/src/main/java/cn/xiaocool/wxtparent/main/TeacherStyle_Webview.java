package cn.xiaocool.wxtparent.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

public class TeacherStyle_Webview extends BaseActivity implements View.OnClickListener {


    public static IWXAPI api_weixin;
    private int NUM_SHARE;
    private TextView tv_fenxiang;
    private RelativeLayout up_jiantou, re_fenxiang;
    private WebView webView;
    private Intent intent;
    private String url;
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_teacher_style_webview);
        intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        Log.e("url", url);
        api_weixin = MainActivity.api_weixin;
        init();

    }

    private void init() {

//微信初始化
//        regToWx();

        tv_fenxiang= (TextView) findViewById(R.id.tv_fenxiang);
        tv_fenxiang.setOnClickListener(this);

        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);

//        re_fenxiang = (RelativeLayout) findViewById(R.id.re_fenxiang);
//        re_fenxiang.setOnClickListener(this);


        webView = (WebView) findViewById(R.id.teaher_style_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.tv_fenxiang:
                Log.e("yyyyy", "re_fenxiang");
                showPopupMenu();
                break;
//            case R.id.re_fenxiang:
//                Log.e("yyyyy", "re_fenxiang");
//                showPopupMenu();
//                break;

        }


    }

    private void showPopupMenu() {

        View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_share_popupmenu, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAtLocation(webView, Gravity.BOTTOM ,0, 0);

//        //设置弹出位置
//        int[] location = new int[2];
//        tv_fenxiang.getLocationOnScreen(location);
//        popupWindow.showAsDropDown(tv_fenxiang);
//
//        re_fenxiang.getLocationOnScreen(location);
//        popupWindow.showAsDropDown(re_fenxiang);

        TextView tv_share_pyq = (TextView) layout.findViewById(R.id.tv_share_pyq);
        TextView tv_share_hy = (TextView) layout.findViewById(R.id.tv_share_hy);

        tv_share_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NUM_SHARE = 1;
                wechatShare(1);
                popupWindow.dismiss();
            }
        });

        tv_share_hy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NUM_SHARE = 0;
                wechatShare(0);
                popupWindow.dismiss();
            }
        });


        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });


    }

    private void wechatShare(int flag) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.apply);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api_weixin.sendReq(req);
    }

    //在需要分享的地方添加代码：
//    wechatShare(0);//分享到微信好友
//    wechatShare(1);//分享到微信朋友圈

}
