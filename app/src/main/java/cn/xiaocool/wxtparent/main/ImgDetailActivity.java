package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.ViewPageAdapter;
import cn.xiaocool.wxtparent.presenter.ImageBrowsePresenter;
import cn.xiaocool.wxtparent.ui.ImageBrowseView;
import cn.xiaocool.wxtparent.utils.ScreenUtils;


public class ImgDetailActivity extends Activity implements ViewPager.OnPageChangeListener,View.OnClickListener,ImageBrowseView {

    private ViewPager vp;
    private TextView hint;
    private TextView save;
    private ViewPageAdapter adapter;
    private ImageBrowsePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_image_browse);
        steepStatusBar();
        vp = (ViewPager) this.findViewById(R.id.viewPager);
        hint = (TextView) this.findViewById(R.id.hint);
        save = (TextView) this.findViewById(R.id.save);
        save.setOnClickListener(this);
        initPresenter();
        presenter.loadImage();
    }

    public void initPresenter(){
        presenter = new ImageBrowsePresenter(this);
    }

    @Override
    public Intent getDataIntent() {
        return getIntent();
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void setImageBrowse(List<String> images,int position) {
        if(adapter == null && images != null && images.size() != 0){
            adapter = new ViewPageAdapter(this,images);
            vp.setAdapter(adapter);
            vp.setCurrentItem(position);
            vp.addOnPageChangeListener(this);
            hint.setText(position + 1 + "/" + images.size());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter.setPosition(position);
        hint.setText(position + 1 + "/" + presenter.getImages().size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        presenter.saveImage();
    }


    public static void startActivity(Context context, ArrayList<String> images, int position){
        Intent intent = new Intent(context, ImgDetailActivity.class);
        intent.putStringArrayListExtra("Imgs",images);
        intent.putExtra("position",position);
        context.startActivity(intent);
    }


    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setStatusHeight();
        }
    }

    /**
     * 设置沉浸式状态栏高度
     */
    private void setStatusHeight() {
        ViewGroup.LayoutParams layoutParams =  findViewById(R.id.status_view).getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(this);
        layoutParams.height = ScreenUtils.getStatusHeight(this);
        findViewById(R.id.status_view).setLayoutParams(layoutParams);
    }
}
