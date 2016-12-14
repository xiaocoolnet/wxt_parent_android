package cn.xiaocool.wxtparent.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class SelectRelationActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_back,rl_yey,rl_nainai,rl_shushu,rl_ayi,rl_laolao,rl_laoye,rl_relation;
    private TextView tv_complete,tv_relation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_relation);
        context = this;
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        rl_ayi = (RelativeLayout) findViewById(R.id.ayi);
        rl_ayi.setOnClickListener(this);
        rl_laolao = (RelativeLayout) findViewById(R.id.laolao);
        rl_laolao.setOnClickListener(this);
        rl_nainai = (RelativeLayout) findViewById(R.id.nainai);
        rl_nainai.setOnClickListener(this);
        rl_laoye = (RelativeLayout) findViewById(R.id.laoye);
        rl_laoye.setOnClickListener(this);
        rl_shushu = (RelativeLayout) findViewById(R.id.shushu);
        rl_shushu.setOnClickListener(this);
        rl_yey = (RelativeLayout) findViewById(R.id.yeye);
        rl_yey.setOnClickListener(this);
        rl_relation = (RelativeLayout) findViewById(R.id.rl_relation);
        rl_relation.setOnClickListener(this);
        tv_complete = (TextView) findViewById(R.id.complete);
        tv_complete.setOnClickListener(this);
        tv_relation = (TextView) findViewById(R.id.tv_relation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.yeye:
                intent("爷爷");
                break;
            case R.id.nainai:
                intent("奶奶");
                break;
            case R.id.ayi:
                intent("阿姨");
                break;
            case R.id.shushu:
                intent("叔叔");
                break;
            case R.id.laolao:
                intent("姥姥");
                break;
            case R.id.laoye:
                intent("姥爷");
                break;
            case R.id.rl_relation:
                showDialog(tv_relation);
                break;
            case R.id.complete:
                if(tv_relation.getText().length()==0){
                    ToastUtils.ToastShort(context,"请输入自定义关系");
                }else {
                    intent(tv_relation.getText().toString());
                }
                break;
        }
    }
    public void intent(String name){
        Intent intent = new Intent();
        intent.putExtra("relation", name);
        setResult(RESULT_OK, intent);
        finish();
    }
    private void showDialog(final TextView tv) {

        // 1.创建弹出式对话框
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);    // 系统默认Dialog没有输入框

        // 获取自定义的布局
        View alertDialogView = View.inflate(context, R.layout.edit_and_send, null);
        final AlertDialog tempDialog = alertDialog.create();
        tempDialog.setView(alertDialogView, 0, 0, 0, 0);

        // 2.密码框-EditText。alertDialogView.findViewById(R.id.自定义布局中的文本框)
        final EditText et_dialog_confirmphoneguardpswd = (EditText) alertDialogView.findViewById(R.id.btn_discuss);

        // 确认按钮，确认验证密码
        Button btn_dialog_resolve_confirmphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_ok);
        btn_dialog_resolve_confirmphoneguardpswd.setOnClickListener(new View.OnClickListener() {
            // 点击按钮处理
            public void onClick(View v) {
                // 提取文本框中输入的文本密码
                if (et_dialog_confirmphoneguardpswd.getText().length() > 0) {
                    //获取到需要上传的参数
                    tv.setText(et_dialog_confirmphoneguardpswd.getText().toString());
                } else {

                    Toast.makeText(context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                }
                tempDialog.dismiss();
            }
        });
        // 取消按钮，不验证密码
        Button btn_dialog_cancel_confirmphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_cancel);
        btn_dialog_cancel_confirmphoneguardpswd.setOnClickListener(new View.OnClickListener() {
            // 点击按钮处理
            public void onClick(View v) {
                //
                tempDialog.dismiss();
            }
        });

        tempDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_dialog_confirmphoneguardpswd, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        /** 3.自动弹出软键盘 **/
        tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_dialog_confirmphoneguardpswd, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        tempDialog.show();
    }
}
