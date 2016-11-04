package cn.xiaocool.wxtparent.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;



import org.json.JSONObject;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.app.ExitApplication;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.main.LoginActivity;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.LogUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;
import cn.xiaocool.wxtparent.view.WxtApplication;

public class ForgetPswActivity extends Activity implements View.OnClickListener {
    private Context context;
    private TextView tv_finish;
    private RelativeLayout rl_back;
    private EditText ed_old,ed_new,ed_confirm;
    private UserInfo userInfo;
    private SharedPreferences sp;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x110:
                    JSONObject object = (JSONObject) msg.obj;
                    if(object.optString("status").equals("success")){
                        ToastUtils.ToastShort(context, "修改密码成功");
                        userInfo.clearDataExceptPhone(ForgetPswActivity.this);
                        SharedPreferences.Editor e = sp.edit();
                        LogUtils.e("删除前", e.toString());
                        e.clear();
                        e.commit();

                        WxtApplication.UID = 0;
                        LogUtils.e("删除后", e.toString());
                        handler.sendEmptyMessageDelayed(0x111, 1000);
                    }
                    break;
                case 0x111:
                    IntentUtils.getIntent(ForgetPswActivity.this, LoginActivity.class);
                    finish();
                    ExitApplication.getInstance().exit();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_psw);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        sp = context.getSharedPreferences("list", context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        tv_finish = (TextView) findViewById(R.id.finish);
        tv_finish.setOnClickListener(this);
        ed_old = (EditText) findViewById(R.id.ed_old);
        ed_new = (EditText) findViewById(R.id.ed_new);
        ed_confirm = (EditText) findViewById(R.id.ed_confirm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.finish:
                String oldPsw = ed_old.getText().toString().trim();
                String newPsw = ed_new.getText().toString().trim();
                String confirmPsw = ed_confirm.getText().toString().trim();
                if(oldPsw.length()==0||oldPsw.equals("")){
                    ToastUtils.ToastShort(context,"原密码不能为空");
                }else if(newPsw.length()==0||newPsw.equals("")){
                    ToastUtils.ToastShort(context,"新密码不能为空");
                }else if(confirmPsw.length()==0||confirmPsw.equals("")){
                    ToastUtils.ToastShort(context,"确认密码不能为空");
                }else if(!confirmPsw.equals(newPsw)){
                    ToastUtils.ToastShort(context,"两次密码不相同");
                }else if(!oldPsw.equals(userInfo.getUserPassword())){
                    ToastUtils.ToastShort(context,"原密码错误，请重新输入原密码");
                }else{
                    new SpaceRequest(context,handler).changePsw(newPsw,0x110);
                }
                break;
        }
    }
}
