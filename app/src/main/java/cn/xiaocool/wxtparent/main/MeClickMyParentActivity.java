package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.BabyParentAdapter;
import cn.xiaocool.wxtparent.bean.ParentInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.MeRequest;
import cn.xiaocool.wxtparent.utils.IntentUtils;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class MeClickMyParentActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private Context mContext;
    private BabyParentAdapter myBabyAdapter;
    private ListView listView;
    private LinearLayout ll_add_child;
    private ArrayList<ParentInfo> arrayList;
    private HashMap<String, Object> hashMap;
    private int size;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETBABYPARENT:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject babyObject;
                        arrayList.clear();
                        int length = jsonArray.length();
                        size = length;
                        for (int i = 0; i < length; i++) {
                            babyObject = (JSONObject) jsonArray.get(i);
                            ParentInfo parentInfo = new ParentInfo();
                            parentInfo.setId(babyObject.optString("userid"));
                            parentInfo.setAppellation(babyObject.optString("appellation"));
                            parentInfo.setType(babyObject.optString("type"));
                            parentInfo.setName(babyObject.optJSONArray("parent_info").optJSONObject(0).optString("name"));
                            parentInfo.setPhone(babyObject.optJSONArray("parent_info").optJSONObject(0).optString("phone"));
                            parentInfo.setPhoto(babyObject.optJSONArray("parent_info").optJSONObject(0).optString("photo"));
                            arrayList.add(parentInfo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myBabyAdapter = new BabyParentAdapter(mContext, arrayList);
                    listView.setAdapter(myBabyAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.baby_parent);
        initView();
        mContext = this;
        new MeRequest(mContext, handler).MyParentAll();
    }

    private void initView() {
        ll_add_child = (LinearLayout) findViewById(R.id.add_child);
        ll_add_child.setOnClickListener(this);
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        //ListView
        listView = (ListView) findViewById(R.id.my_baby_content);
        arrayList = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new MeRequest(mContext, handler).MyParentAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            case R.id.add_child:
                if(size>4){
                    ToastUtils.ToastShort(mContext,"您无法再添加家长了");
                }else {
                    IntentUtils.getIntent(MeClickMyParentActivity.this, AddChildActivity.class);
                }
                break;
        }

    }


}
