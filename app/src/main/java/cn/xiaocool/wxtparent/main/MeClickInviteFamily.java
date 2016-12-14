package cn.xiaocool.wxtparent.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.net.HttpTool;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by Administrator on 2016/3/23.
 */
public class MeClickInviteFamily extends BaseActivity {
    private ImageView btn_exit;
    private EditText invitefamily_et_telephone;
    private EditText invitefamily_et_name;
    private Button btn_besure;
    private Button btn_cancle;
    private String telephone = "";
    private String name = "";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invitefamily);
        initView();


    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        invitefamily_et_telephone = (EditText) findViewById(R.id.invitefamily_et_telephone);
        invitefamily_et_name = (EditText) findViewById(R.id.invitefamily_et_name);
        btn_besure = (Button) findViewById(R.id.btn_besure);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_besure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent =getIntent();
//
//                Bundle bundle = new Bundle();
//                telephone = invitefamily_et_telephone.getText().toString();
//                name = invitefamily_et_name.getText().toString();
//                intent.putExtra("telephone", telephone);
//                intent.putExtra("name", name);
//                bundle.putString("telephone",telephone);
////                intent.putExtras(bundle);
//                MeClickInviteFamily.this.setResult(RESULT_OK, intent);
//                MeClickInviteFamily.this.finish();
                inputPhoneAndName();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitefamily_et_telephone.setText("");
                invitefamily_et_name.setText("");
            }
        });
    }

    public void inputPhoneAndName() {
        telephone = invitefamily_et_telephone.getText().toString();
        name = invitefamily_et_name.getText().toString();
        if (telephone.length()==11&&name!=null){
            if (HttpTool.isConnnected(MeClickInviteFamily.this)){
                new SpaceRequest(MeClickInviteFamily.this,handler).inputPhoneNumAndName();
            } else {
                handler.sendEmptyMessage(2);
            }
//        input_text = etHeight.getText().toString();
//        if (input_text.length() != 0 && input_text.length() < 4) {
//            if (HttpTool.isConnnected(mContext)) {
//                Log.e("enter进入", input_text);
//                new SpaceRequest(mContext, handler).InputHeight();
//            } else {
//                handler.sendEmptyMessage(2);
//            }
//        } else {
//            handler.sendEmptyMessage(3);
        }else if (telephone.length()!=11){
            ToastUtils.ToastShort(MeClickInviteFamily.this,"手机号码格式不正确");
        }else if (name.equals("")){
            ToastUtils.ToastShort(MeClickInviteFamily.this,"家人姓名不能为空");
        }

    }
}
