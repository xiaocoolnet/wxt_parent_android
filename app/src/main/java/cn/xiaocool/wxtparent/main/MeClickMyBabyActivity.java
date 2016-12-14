package cn.xiaocool.wxtparent.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.MyBabyAdapter;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.MeRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

/**
 * Created by wzh on 2016/1/29.
 */
public class MeClickMyBabyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_exit;
    private String[] name,id,childAvator,className,schoolName,classId,schoolId;
    private Context mContext;
    private MyBabyAdapter myBabyAdapter;
    private ListView listView;
    private LinearLayout ll_add_child;
    private ArrayList<HashMap<String, Object>> arrayList;
    private UserInfo userInfo;
    private HashMap<String, Object> hashMap;
    private static final int MSG_SET_ALIAS = 1001;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.BABY_INFO:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject babyObject;
                        int length = jsonArray.length();
                        name = new String[length];
                        id = new String[length];
                        childAvator = new String[length];
                        className = new String[length];
                        schoolName = new String[length];
                        classId = new String[length];
                        schoolId = new String[length];
                        for (int i = 0; i <= length; i++) {
                            babyObject = (JSONObject) jsonArray.get(i);
                            id[i] = babyObject.getString("studentid");
                            name[i] = babyObject.getString("studentname");
                            childAvator[i] = babyObject.getString("studentavatar");
                            hashMap = new HashMap<String, Object>();
                            hashMap.put("babyName", name[i]);
                            hashMap.put("babyAvator", childAvator[i]);
                            hashMap.put("babyId", id[i]);
                            hashMap.put("schoolName", babyObject.getString("school_name"));
                            if (babyObject.optJSONArray("classlist").length()>0){
                                JSONObject classObject = babyObject.optJSONArray("classlist").optJSONObject(0);
                                className[i] = classObject.optString("classname");
                                schoolId[i] = classObject.optString("schoolid");
                                classId[i] = classObject.optString("classid");
                                hashMap.put("className", classObject.optString("classname"));
                                hashMap.put("schoolid", classObject.optString("schoolid"));
                                hashMap.put("classid", classObject.optString("classid"));
                            }

                            arrayList.add(hashMap);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myBabyAdapter = new MyBabyAdapter(mContext, arrayList);
                    listView.setAdapter(myBabyAdapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.me_mybaby);
        initView();
        mContext = this;
        userInfo = new UserInfo(mContext);
        new MeRequest(mContext, handler).MyBabyAll();
    }

    private void initView() {
        /*ll_add_child = (LinearLayout) findViewById(R.id.add_child);
        ll_add_child.setOnClickListener(this);*/
        btn_exit = (ImageView) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        //ListView
        listView = (ListView) findViewById(R.id.my_baby_content);
        arrayList = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idHello) {
                if (schoolId[position]==null){
                    ToastUtils.ToastShort(mContext, "宝宝信息不完全，切换失败！");
                    return;
                }
                userInfo.setChildId(id[position]);
                userInfo.setChildName(name[position]);
                userInfo.setChildAvator(childAvator[position]);
                userInfo.setSchoolId(schoolId[position]);
                userInfo.setClassId(classId[position]);
                userInfo.setClassName(className[position]);
                userInfo.writeData(mContext);
                ToastUtils.ToastShort(mContext, "切换成功！");
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                finish();
                break;
            /*case R.id.add_child:
                //IntentUtils.getIntent(MeClickMyBabyActivity.this,AddChildActivity.class);
                break;*/
        }

    }


}
