package cn.xiaocool.wxtparent.main;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

public class AboutUsActivity extends BaseActivity {
    private ImageView btn_back;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_us);

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
        webView.loadUrl("http://wxt.xiaocool.net/index.php?g=portal&m=OurParent&a=index");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回的是true，说明是使用webview打开的网页。否则使用系统默认的浏览器
                return true;
            }
        });
    }
}
