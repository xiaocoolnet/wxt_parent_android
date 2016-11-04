package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
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
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class OnlineCommentActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private RelativeLayout btn_exit;
    private TextView submit;
    private EditText editText;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CommunalInterfaces.ONLINECOMMENT:
                    JSONObject object = (JSONObject) msg.obj;
                    if(object.optString("status").equals("success")){
                        ToastUtils.ToastShort(mContext,"留言成功，谢谢您对本软件的支持！");
                        finish();
                    }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_online_comment);
        mContext = this;
        initView();
    }
    private void initView() {
        btn_exit = (RelativeLayout) findViewById(R.id.btn_exit);
        btn_exit .setOnClickListener(this);
        editText = (EditText) findViewById(R.id.ed_content);
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
                break;
            case R.id.submit:
                String content = editText.getText().toString();
                if(content.trim().length()==0||content.equals("")){
                    ToastUtils.ToastShort(mContext,"请输入留言内容！");
                }else{
                    new SpaceRequest(mContext,handler).onlineComment(content);
                }
                break;
        }
    }
}
