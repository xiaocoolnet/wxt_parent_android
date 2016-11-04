package cn.xiaocool.wxtparent.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;

public class ClassEvent_Baoming_Activity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout re_csz, up_jiantou;
    private EditText ed_nl, ed_jzname, ed_phono;
    private Button btn_baoming;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_event__baoming_);

        init();
    }

    private void init() {
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        re_csz = (RelativeLayout) findViewById(R.id.re_csz);
        ed_nl = (EditText) findViewById(R.id.ed_nl);
        ed_jzname = (EditText) findViewById(R.id.ed_jzname);
        ed_phono = (EditText) findViewById(R.id.ed_phono);
        btn_baoming = (Button) findViewById(R.id.btn_baoming);

        up_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_baoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "暂无发送接口", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
