package cn.xiaocool.wxtparent.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

public class Space_news_xitong_content extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_space_news_xitong_content);
    }
}
