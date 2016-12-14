package cn.xiaocool.wxtparent.main;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

/**
 * Created by wzh on 2016/3/13.
 */
public class SpaceClickContacts extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_news_contacts);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
                break;
        }
    }
}
