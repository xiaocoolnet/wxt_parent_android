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

import org.json.JSONException;
import org.json.JSONObject;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.HttpTool;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class SpaceClickInputHeightActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private Button btn_finish;
    private Context mContext;
    private EditText etHeight;
    public static String input_text;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.INPUT_HEIGHT:
                    JSONObject obj_height = (JSONObject) msg.obj;
                    try {
                        String status = obj_height.getString("status");
                        if (status.equals("success")) {
                            ToastUtils.ToastShort(mContext, "录入身高数据成功！");
                            finish();
                        } else {
                            ToastUtils.ToastShort(mContext, "录入身高失败，请重试！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    ToastUtils.ToastShort(mContext, "网络连接失败");
                    break;
                case 3:
                    ToastUtils.ToastShort(mContext, "请输入正确的数据（单位cm）！");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.space_click_input_height);
        initView();
        mContext = this;
    }

    private void initView() {
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        etHeight = (EditText) findViewById(R.id.et_height);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_finish:
                inputHeight();
                break;


        }

    }

    public void inputHeight() {
        input_text = etHeight.getText().toString();
        if (input_text.length() != 0 && input_text.length() < 4) {
            if (HttpTool.isConnnected(mContext)) {
                Log.e("enter进入", input_text);
                new SpaceRequest(mContext, handler).InputHeight();
            } else {
                handler.sendEmptyMessage(2);
            }
        } else {
            handler.sendEmptyMessage(3);
        }

    }


}
