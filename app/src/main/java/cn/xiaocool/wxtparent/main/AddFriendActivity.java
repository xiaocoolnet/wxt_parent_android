package cn.xiaocool.wxtparent.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaocool.wxtparent.BaseActivity;
import cn.xiaocool.wxtparent.R;
import cn.xiaocool.wxtparent.adapter.StudentInfoAdapter;
import cn.xiaocool.wxtparent.bean.Friend;
import cn.xiaocool.wxtparent.bean.StudentInfo;
import cn.xiaocool.wxtparent.bean.UserInfo;
import cn.xiaocool.wxtparent.dao.CommunalInterfaces;
import cn.xiaocool.wxtparent.net.request.SpaceRequest;
import cn.xiaocool.wxtparent.utils.ToastUtils;

public class AddFriendActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private ListView listView;
    private RelativeLayout rl_back;
    private UserInfo userInfo;
    private ArrayList<StudentInfo> studentInfos;
    private ArrayList<Friend> friends;
    private StudentInfoAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.GETSTUDENTLIST:
                    JSONObject obj = (JSONObject) msg.obj;
                    String status = obj.optString("status");
                    if (status.equals(CommunalInterfaces._STATE)) {
                        JSONArray stuArray = obj.optJSONArray("data");
                        studentInfos.clear();
                        for(int i = 0;i<stuArray.length();i++){
                            JSONObject stuObject = stuArray.optJSONObject(i);
                            StudentInfo studentInfo = new StudentInfo();
                            studentInfo.setId(stuObject.optString("id"));
                            studentInfo.setName(stuObject.optString("name"));
                            studentInfo.setPhoto(stuObject.optString("photo"));
                            if(isFriend(stuObject.optString("id"))){
                                studentInfo.setIsFriend(1);
                            }else{
                                studentInfo.setIsFriend(0);
                            }
                            if(!stuObject.optString("id").equals(userInfo.getChildId())){
                                studentInfos.add(studentInfo);
                            }
                        }
                    }
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }else {
                        adapter = new StudentInfoAdapter(context,studentInfos,handler);
                        listView.setAdapter(adapter);
                    }
                    break;
                case CommunalInterfaces.ADDFRIEND:
                    JSONObject obj1 = (JSONObject) msg.obj;
                    String status1 = obj1.optString("status");
                    if (status1.equals(CommunalInterfaces._STATE)) {
                        ToastUtils.ToastShort(context, "添加成功！");
                    }
                    break;
            }
        }
    };

    private boolean isFriend(String id) {
        for(int i=0;i<friends.size();i++) {
            if(id.equals(friends.get(i).getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        friends = (ArrayList<Friend>) getIntent().getSerializableExtra("list");
        studentInfos = new ArrayList<>();
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.my_baby_content);
        rl_back = (RelativeLayout) findViewById(R.id.up_jiantou);
        rl_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_jiantou:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new SpaceRequest(context,handler).getStudentList();
    }
}
