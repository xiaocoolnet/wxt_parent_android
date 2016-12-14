package cn.xiaocool.wxtparent.main;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by wzh on 2016/2/17.
 */
public class MeHelpBackActivity extends BaseActivity {
    private ImageView btn_back;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.me_help_click);
        webView = (WebView) findViewById(R.id.webView);
        btn_back = (ImageView)findViewById(R.id.me_help_setting_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl("http://wxt.xiaocool.net/index.php?g=portal&m=help&a=index");
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回的是true，说明是使用webview打开的网页。否则使用系统默认的浏览器
                return true;
            }
        });

    }


}
