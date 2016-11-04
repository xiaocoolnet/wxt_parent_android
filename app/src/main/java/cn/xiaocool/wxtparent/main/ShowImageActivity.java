package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.xiaocool.wxtparent.R;

public class ShowImageActivity extends Activity implements View.OnClickListener {

    private Context context;
    private ImageView imageView,back;
    private String imagePath;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_image);
        context=this;
        initView();
    }

    private void initView() {
        imagePath=getIntent().getStringExtra("Image");
        back = (ImageView) findViewById(R.id.activity_show_image_back);
        back.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.activity_show_image_big_image);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage("http://wxt.xiaocool.net/uploads/microblog/" + imagePath, imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_show_image_back:
                finish();
                break;
        }
    }
}
