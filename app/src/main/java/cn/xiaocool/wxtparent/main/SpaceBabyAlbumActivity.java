package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.fragment.Baby_album_fragment;
import cn.xiaocool.wxtparent.fragment.Class_album_fragment;
import cn.xiaocool.wxtparent.ui.ProgressViewUtil;

/**
 * Created by wzh on 2016/2/19.
 */
public class SpaceBabyAlbumActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_tianjia;
    private RelativeLayout up_jiantou;
    private Baby_album_fragment baby_album_fragment;
    private Class_album_fragment class_album_fragment;
    private RadioButton rb1, rb2;
    private FragmentTransaction trx;
    private View line_laoshi, line_haoyou;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_baby_album);
        ProgressViewUtil.show(this);
        initView();
    }

    private void initView() {

        iv_tianjia = (ImageView) findViewById(R.id.iv_tianjia);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        iv_tianjia.setOnClickListener(this);
        up_jiantou.setOnClickListener(this);
        line_laoshi = findViewById(R.id.line_laoshi);
        line_haoyou = findViewById(R.id.line_haoyou);
        baby_album_fragment = new Baby_album_fragment();
        class_album_fragment = new Class_album_fragment();
        rb1 = (RadioButton) findViewById(R.id.space_btn_album_1);
        rb2 = (RadioButton) findViewById(R.id.space_btn_album_2);

        trx = getSupportFragmentManager().beginTransaction();

        trx.add(R.id.layout_fragment_album, baby_album_fragment)
                .add(R.id.layout_fragment_album, class_album_fragment)
                .show(baby_album_fragment).hide(class_album_fragment)
                .commit();

    }

    public void onClick(View v) {
        trx = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;

            case R.id.space_btn_album_1:
                rb1.setTextColor(getResources().getColor(R.color.title_bg_color));
                rb2.setTextColor(Color.BLACK);
                line_laoshi.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                line_haoyou.setBackgroundColor(getResources().getColor(R.color.zsq_bg_color));
                trx.hide(class_album_fragment).show(baby_album_fragment).commit();
                break;
            case R.id.space_btn_album_2:
                rb2.setTextColor(getResources().getColor(R.color.title_bg_color));
                rb1.setTextColor(Color.BLACK);
                line_laoshi.setBackgroundColor(getResources().getColor(R.color.zsq_bg_color));
                line_haoyou.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                trx.hide(baby_album_fragment).show(class_album_fragment).commit();
                break;
            case R.id.iv_tianjia:
                Intent intent = new Intent(SpaceBabyAlbumActivity.this, AddAlbumActivity.class);
                startActivity(intent);
        }
    }
}
