package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.w3c.dom.Text;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.net.request.constant.NetBaseConstant;

/**
 * Created by wzh on 2016/3/28.
 */
public class SpaceClickRecipesItemActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img, btnExit;
    private TextView tvTitle, tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_click_recipes_item);
        //初始化视图
        btnExit = (ImageView) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.recipes_content_img);
        tvTitle = (TextView) findViewById(R.id.recipes_content_title);
        tvInfo = (TextView) findViewById(R.id.recipes_content_info);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String info = intent.getStringExtra("info");
        String pic = intent.getStringExtra("pic");
        tvTitle.setText(title);
        tvInfo.setText(info);
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_RECIPES_HOST + pic, img, new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square)
                .cacheInMemory(true).cacheOnDisc(true).build());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
        }

    }
}
