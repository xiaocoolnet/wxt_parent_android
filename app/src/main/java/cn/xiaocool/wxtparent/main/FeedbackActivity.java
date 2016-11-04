package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xiaocool.wxtparent.R;

public class FeedbackActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private RelativeLayout btn_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);
        mContext = this;
        initView();
    }

    private void initView() {
        btn_exit = (RelativeLayout) findViewById(R.id.btn_exit);
        btn_exit .setOnClickListener(this);
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
