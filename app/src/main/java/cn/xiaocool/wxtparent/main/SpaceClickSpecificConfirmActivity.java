package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/4/19.
 */
public class SpaceClickSpecificConfirmActivity extends BaseActivity implements View.OnClickListener {
    private TextView baby_id;
    private TextView confirm_relation;
    private TextView confirm_delivery_time;
    private ImageView btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_specific_confirm_click);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        intent.getStringExtra("confirm_teacher_id");
        intent.getStringExtra("confirm_user_id");
        intent.getStringExtra("confirm_photo");
        intent.getStringExtra("confirm_delivery_status");
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        baby_id = (TextView) findViewById(R.id.baby_id);
        baby_id.setText(intent.getStringExtra("confirm_id"));
        confirm_relation = (TextView) findViewById(R.id.confirm_relation);
        confirm_relation.setText(intent.getStringExtra("confirm_user_id"));
        confirm_delivery_time = (TextView) findViewById(R.id.confirm_delivery_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        confirm_delivery_time.setText(dateFormat.format(new Date(Long.parseLong(intent.getStringExtra("confirm_delivery_time")) * 1000)));
    }

    public void beSure(View view) {
        //执行确认操作
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
