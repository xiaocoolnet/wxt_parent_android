package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

public class Space_news_xitong extends BaseActivity implements View.OnClickListener {

    private RelativeLayout up_jiantou,re_liebiao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_space_news_xitong);


        init();
    }

    private void init() {
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        re_liebiao = (RelativeLayout) findViewById(R.id.re_liebiao);
        re_liebiao.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.re_liebiao:
                Intent intent=new Intent(Space_news_xitong.this,Space_news_xitong_content.class);
                startActivity(intent);


        }
    }
}
