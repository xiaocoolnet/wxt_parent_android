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
public class SpaceBabyAlbumActivity extends BaseActivity implements View.OnClickListener ,View.OnLayoutChangeListener{

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
        //获取屏幕高度
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
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
        activityRootView = findViewById(R.id.layout_root);
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

    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);


        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){

            if (baby_album_fragment.mAdapter!=null&&baby_album_fragment.mAdapter.commentPopupWindow!=null&&baby_album_fragment.mAdapter.commentPopupWindow.isShowing()) {
                baby_album_fragment.mAdapter.commentPopupWindow.dismiss();
            }
            if (class_album_fragment.mAdapter!=null&&class_album_fragment.mAdapter.commentPopupWindow!=null&&class_album_fragment.mAdapter.commentPopupWindow.isShowing()) {
                class_album_fragment.mAdapter.commentPopupWindow.dismiss();
            }
        }

    }
}
