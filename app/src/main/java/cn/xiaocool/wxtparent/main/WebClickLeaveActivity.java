package cn.xiaocool.wxtparent.main;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by wzh on 2016/1/29.
 */
public class WebClickLeaveActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_open_leave);
        initView();

    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
        }

    }


}
