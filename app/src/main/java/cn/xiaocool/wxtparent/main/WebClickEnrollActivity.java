package cn.xiaocool.wxtparent.main;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;

public class WebClickEnrollActivity extends BaseActivity implements View.OnClickListener {


    private EditText ed_name, ed_sex, ed_birth, ed_address, ed_class, ed_phone, ed_qq, ed_weixin, ed_educate, ed_school, ed_remark;
    private ImageView image;
    private RelativeLayout up_jiantou;
    private Button btn_baoming;
    private TextView tv_content;
    private String name, sex = "?", birth, address, class_, phone, qq, weixin, educate, school, remark;
    private RadioButton rb_nan, rb_nv;
    private RadioGroup radioGroup;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.ENROLL:
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj.optString("status").equals("success")) {
                        Log.e("yy", "11111");
                        finish();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_click_enroll);

        init();


    }

    private void init() {
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_birth = (EditText) findViewById(R.id.ed_birth);
        ed_address = (EditText) findViewById(R.id.ed_address);
        ed_class = (EditText) findViewById(R.id.ed_class);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_qq = (EditText) findViewById(R.id.ed_qq);
        ed_weixin = (EditText) findViewById(R.id.ed_weixin);
        ed_educate = (EditText) findViewById(R.id.ed_educate);
        ed_school = (EditText) findViewById(R.id.ed_school);
        ed_remark = (EditText) findViewById(R.id.ed_remark);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_nan = (RadioButton) findViewById(R.id.rb_nan);
        rb_nv = (RadioButton) findViewById(R.id.rb_nv);

        image = (ImageView) findViewById(R.id.image);
        up_jiantou = (RelativeLayout) findViewById(R.id.up_jiantou);
        up_jiantou.setOnClickListener(this);
        btn_baoming = (Button) findViewById(R.id.btn_baoming);
        btn_baoming.setOnClickListener(this);


        tv_content = (TextView) findViewById(R.id.tv_content);
        Log.e("yy", "22222");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == rb_nan.getId()) {
                    sex = "0";//男 暂时定为0  （接口中不清楚）
                } else if (checkedId == rb_nv.getId()) {
                    sex = "1";//女 暂时定为1  （接口中不清楚）
                }
                Log.e("yy", sex+"____");
            }


        });

        Log.e("yy", sex);
        name = ed_name.getText().toString();
        birth = ed_birth.getText().toString();
        address = ed_address.getText().toString();
        class_ = ed_class.getText().toString();
        phone = ed_phone.getText().toString();
        qq = ed_qq.getText().toString();
        weixin = ed_weixin.getText().toString();
        educate = ed_educate.getText().toString();
        school = ed_school.getText().toString();
        remark = ed_remark.getText().toString();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.btn_baoming:
                Log.e("yyy", "??????");
                new SpaceRequest(getApplicationContext(), handler).enroll(name, sex, birth, address, class_, phone, qq, weixin, educate, school, remark);
                Log.e("yyy", "??????_");
                break;

        }


    }
}
