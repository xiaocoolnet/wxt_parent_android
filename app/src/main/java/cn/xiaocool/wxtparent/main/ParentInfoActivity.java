package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.bean.ParentInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class ParentInfoActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_back,rl_bangding,rl_zhuhao,rl_shanchu;
    private ParentInfo parentInfo;
    private TextView tv_title;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommunalInterfaces.SELETEPARENT:
                    JSONObject obj = (JSONObject) msg.obj;
                    String status = obj.optString("status");
                    if (status.equals(CommunalInterfaces._STATE)) {
                        ToastUtils.ToastShort(context, "删除成功！");
                        finish();
                    }
                    break;
                case CommunalInterfaces.SETMAIN:
                    JSONObject obj1 = (JSONObject) msg.obj;
                    String status1 = obj1.optString("status");
                    if (status1.equals(CommunalInterfaces._STATE)) {
                        ToastUtils.ToastShort(context, "设置成功！");
                        finish();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_info);
        context = this;
        parentInfo = (ParentInfo) getIntent().getSerializableExtra("parent");
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
        rl_shanchu = (RelativeLayout) findViewById(R.id.rl_shanchu);
        rl_shanchu.setOnClickListener(this);
        rl_zhuhao = (RelativeLayout) findViewById(R.id.rl_zhuhao);
        rl_zhuhao.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText(parentInfo.getAppellation());
        if(parentInfo.getType().equals("1")){
            rl_shanchu.setVisibility(View.GONE);
            rl_zhuhao.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
            case R.id.rl_shanchu:
                new SpaceRequest(context,handler).deleteParent(parentInfo.getId());
                break;
            case R.id.rl_zhuhao:
                new SpaceRequest(context,handler).setMainParent(parentInfo.getId());
                break;
        }
    }
}
