package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.net.HttpTool;
import cn.xiaocool.wxtparent.view.WxtApplication;

/**
 * Created by wzh on 2016/1/25.
 */
public class ResetPasswordActivity extends Activity implements View.OnClickListener {
    private EditText edit_password1;
    private EditText edit_password2;
    private Button btn_finish;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(ResetPasswordActivity.this,"设置成功，正在跳转首页！",Toast.LENGTH_SHORT).show();
                    Intent intent_finsh = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                    startActivity(intent_finsh);
            }

        }


    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password);
        init();
    }

    // 实例化控件
    private void init() {
        edit_password1 = (EditText) findViewById(R.id.edit_password1);
        edit_password2 = (EditText) findViewById(R.id.edit_password2);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);
        ImageView btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_finish:

                String firstPassword = edit_password1.getText().toString();
                String secondPassword = edit_password2.getText().toString();
                if (firstPassword.equals(secondPassword) ) {


                    new Thread() {
                        @Override
                        public void run() {

                            String firstPassword = edit_password1.getText().toString();
                            HttpTool.ResetPassword( WxtApplication.UID,firstPassword);
                            handler.sendEmptyMessage(1);
                        }
                    }.start();
                }
                else
                    Toast.makeText(ResetPasswordActivity.this,"请检查输入的密码是否正确！",Toast.LENGTH_SHORT).show();

            case R.id.btn_exit:
                finish();
                break;
        }


    }
}
