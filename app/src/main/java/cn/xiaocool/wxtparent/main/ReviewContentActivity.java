package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xiaocool.wxtparent.R;

/**
 * Created by wzh on 2016/3/16.
 */
public class ReviewContentActivity extends Activity implements View.OnClickListener {

    private TextView tvName,tvContent,tvDate;
    private ImageView ivExit;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_review_itemclick);
        tvContent = (TextView) findViewById(R.id.text_content);
        tvName = (TextView) findViewById(R.id.text_name);
        tvDate = (TextView) findViewById(R.id.text_date);
        ivExit = (ImageView) findViewById(R.id.btn_exit);
        ivExit.setOnClickListener(this);
        Intent intent = getIntent();
        String intentContent = intent.getStringExtra("content");
        String intentName = intent.getStringExtra("name");
        String intentDate = intent.getStringExtra("date");
        tvContent.setText(intentContent);
        tvName.setText(intentName);
        tvDate.setText(intentDate);
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
