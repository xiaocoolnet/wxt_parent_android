package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.JsonParser;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class ChangeChatNameActivity extends BaseActivity {

    private EditText edt_chat_name;
    private Context context;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    if (JsonParser.JSONparser(context, msg.obj.toString())){
                        ToastUtils.ToastShort(context, "修改成功");
                        Intent intent = new Intent();
                        intent.putExtra("chat_name", edt_chat_name.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else {
                        ToastUtils.ToastShort(context,"修改失败");
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_chat_name);
        context = this;
        edt_chat_name = (EditText) findViewById(R.id.edt_chat_name);
        edt_chat_name.setText(getIntent().getStringExtra("chat_name"));
        findViewById(R.id.up_jiantou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChatName();
            }
        });
    }

    private void saveChatName() {
        if (edt_chat_name.getText()==null||edt_chat_name.getText().equals("")){
            ToastUtils.ToastShort(context,"请输入名称");
        }else if (edt_chat_name.getText().equals(getIntent().getStringExtra("chat_name"))){
            ToastUtils.ToastShort(context,"群名称未修改！");
        }else {
            String url = "http://wxt.xiaocool.net/index.php?g=apps&m=message&a=UpdateGroupTitle";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uid",new UserInfo(context).getUserId()));
            params.add(new BasicNameValuePair("groupid",getIntent().getStringExtra("chatid")));
            params.add(new BasicNameValuePair("title",edt_chat_name.getText().toString()));
            new SpaceRequest(context,handler).post(url,params,101);
        }

    }
}
