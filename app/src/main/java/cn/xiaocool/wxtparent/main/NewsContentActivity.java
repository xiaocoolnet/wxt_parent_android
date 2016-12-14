package cn.xiaocool.wxtparent.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by wzh on 2016/3/16.
 */
public class NewsContentActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvName,tvContent;
    private ImageView ivExit;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_news_contacts);
        tvName = (TextView) findViewById(R.id.text_name);
        tvContent = (TextView) findViewById(R.id.text_content);
        ivExit = (ImageView) findViewById(R.id.btn_exit);
        ivExit.setOnClickListener(this);
        Intent intent = getIntent();
        String intentName = intent.getStringExtra("name");
        String intentContent = intent.getStringExtra("content");
        tvName.setText(intentName);
        tvContent.setText(intentContent);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
                break;
        }
    }
}
