package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SpaceHappySchoolSpecificActivity extends BaseActivity {
    private TextView title;
    private ImageView happySchoolImageView;
    private TextView content;
    private TextView releaseName;
    private ImageView btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_class_specificevent);
        initView();
        Intent intent = getIntent();
        final String happy_pic = intent.getStringExtra("happy_pic");
        ImageLoader.getInstance().displayImage("http://www.xiaocool.cn:8016/uploads/ClassAction/chunyou.jpg",happySchoolImageView,new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.drawable.default_square).showImageOnFail(R.drawable.default_square)
                .cacheInMemory(true).cacheOnDisc(true).build());
        title.setText(intent.getStringExtra("happy_title"));
        content.setText(intent.getStringExtra("happy_content"));
        releaseName.setText("\t\t"+"--"+intent.getStringExtra("releasename"));
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        title = (TextView) findViewById(R.id.space_class_title);
        happySchoolImageView = (ImageView) findViewById(R.id.space_class_imageView);
        content = (TextView) findViewById(R.id.space_class_content);
        releaseName = (TextView) findViewById(R.id.space_class_releasename);
    }
}
