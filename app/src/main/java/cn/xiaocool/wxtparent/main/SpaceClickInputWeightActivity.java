package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xiaocool.wxtparent.bean.UserInfo;

import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.net.HttpTool;
import cn.xiaocool.wxtparent.net.NetUtil;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.net.request.constant.WebHome;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickInputWeightActivity extends Activity implements View.OnClickListener {

    private ImageView btn_exit;
    private Button btn_finish;
    public EditText et_weight;
    public static String input_text;
    private Context mContext;
    private UserInfo user;
    private String id;
    private String result_data;
    private SpaceRequest sr;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case CommunalInterfaces.INPUT_WEIGHT:
                    JSONObject obj_weight = (JSONObject)msg.obj;
                    try {
                        String status = obj_weight.getString("status");
                        if (status.equals("success")){
                        ToastUtils.ToastShort(mContext,"录入体重数据成功！");
                            finish();
                        }
                        else {
                            ToastUtils.ToastShort(mContext,"录入体重失败，请重试！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    ToastUtils.ToastShort(mContext, "网络连接失败");
                    break;
                case 3:
                    ToastUtils.ToastShort(mContext, "请输入正确的数据（单位kg）！");
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_click_input_weight);
        initView();
        mContext = this;
    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_weight.setOnClickListener(this);
        user = new UserInfo();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_finish:
                input_weight();
                break;

        }

    }

    public void input_weight() {
        input_text = et_weight.getText().toString();
        if(input_text.length() !=0 && input_text.length() < 3){
            if (HttpTool.isConnnected(mContext)){
                new SpaceRequest(mContext,handler).InputWeight();
            }else {
                handler.sendEmptyMessage(2);
            }
        }else {
            handler.sendEmptyMessage(3);
        }



    }


}
