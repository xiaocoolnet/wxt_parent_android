package cn.xiaocool.wxtparent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import cn.xiaocool.wxtparent.net.request.SpaceRequest;

public class ParadiseWebViewActivity extends Activity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_back;
    private WebView webView;
    private TextView tv_title;
    private String title;
    private String url;
    private ImageView nodata;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x110:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals("success")) {
                        JSONObject object = obj.optJSONObject("data");
                        url = object.optString("url");
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                        webView.loadUrl(url);
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                //返回的是true，说明是使用webview打开的网页。否则使用系统默认的浏览器
                                return true;
                            }
                            @Override
                            public void onReceivedError(WebView view, int errorCode,
                                                        String description, String failingUrl) {
                                // TODO Auto-generated method stub
                                super.onReceivedError(view, errorCode, description, failingUrl);
                                nodata.setVisibility(View.VISIBLE);
                                webView.setVisibility(View.GONE);
                            }
                        });
                    }else{
                        nodata.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_paradise_web_view);
        context = this;
        getData();
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        tv_title = (TextView) findViewById(R.id.title_bar_name);
        tv_title.setText(title);
        webView = (WebView) findViewById(R.id.webView);
        rl_back.setOnClickListener(this);
        nodata = (ImageView) findViewById(R.id.nodata);
    }

    private void getData() {
        title = getIntent().getStringExtra("title");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new SpaceRequest(context, handler).getParadise(title, 0x110);
    }

}
